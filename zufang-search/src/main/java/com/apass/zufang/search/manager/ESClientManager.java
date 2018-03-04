package com.apass.zufang.search.manager;


import com.apass.zufang.search.enums.IndexType;
import com.apass.zufang.search.utils.Esprop;
import com.apass.zufang.search.utils.PropertiesUtils;
import com.google.common.base.Preconditions;
import org.apache.commons.io.IOUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.Properties;

/**
 * Created by xianzhi.wang on 2017/5/15.
 *
 */

public class ESClientManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ESClientManager.class);
    private static Esprop esprop = new Esprop();
    private static volatile TransportClient client;

    static {
        InputStream esIn = ESClientManager.class.getClassLoader().getResourceAsStream("esmapper/es.properties");
        try {
            Properties properties = PropertiesUtils.readFromText(IOUtils.toString(esIn).trim());
            esprop.setClusterName(properties.getProperty("esName"));
            esprop.setHost(properties.getProperty("esHost"));
            esprop.setPort(Integer.valueOf(properties.getProperty("esPort")));
            esprop.setIndice(properties.getProperty("esIndice"));
        } catch (IOException e) {
            LOGGER.error("get es config error ...");
        }
    }

    private ESClientManager() {
    }

    /**
     * 创建一个java操作Es的客户端，已经设置好了，索引、type、_mapping
     * @return
     */
    public static Client getClient() {
        if (client == null) {
            synchronized (ESClientManager.class) {
                if (client == null) {
                    try {
                        LOGGER.info("esName {}", esprop.getClusterName());
                        LOGGER.info("host {}", esprop.getHost());
                        LOGGER.info("port {}", esprop.getPort());
                        Settings settings = Settings
                                .builder()
                                .put("cluster.name", esprop.getClusterName())
                                .put("client.transport.ignore_cluster_name", true)
                                .put("client.transport.sniff", true).build();
                        client = new PreBuiltTransportClient(settings);
                        client = client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(esprop.getHost()), esprop.getPort()));
                        int nodeSize = client.connectedNodes().size();
                        LOGGER.info("nodeSize {}", nodeSize);
                        Preconditions.checkArgument(nodeSize >= 1, "this is no available node");
                        initIndex();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }
        return client;
    }

    /**
     * 初始化索引
     *创建索引，type。设置_mapping内容
     * @throws Exception
     */
    private static void initIndex() throws Exception {
        String indice = esprop.getIndice();
        IndicesAdminClient c = client.admin().indices();
        //创建一个空的
        boolean a = c.prepareExists(indice).get().isExists();
        LOGGER.info("index {} isExists  {}",indice, a);
        if (!c.prepareExists(indice).get().isExists()) {
            CreateIndexResponse createIndexResponse =c.prepareCreate(indice).get();
            LOGGER.info("create index {}", createIndexResponse);
        }
        for (IndexType type : IndexType.values()) {
            TypesExistsResponse typesExistsResponse = c.typesExists(new TypesExistsRequest(new String[]{indice}, type.getDataName())).get();
            if (typesExistsResponse.isExists()) {
                continue;
            }
            String esMapper = type.getMapper();
            InputStream in = ESClientManager.class.getResourceAsStream(esMapper);
            String mappingStr = IOUtils.toString(in).trim();
            IOUtils.closeQuietly(in);
            c.preparePutMapping(indice).setType(type.getDataName()).setSource(mappingStr).get();
        }
    }


}

