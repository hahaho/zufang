package com.apass.zufang.nauth.weex;

import com.apass.gfb.framework.utils.GsonUtils;
import com.apass.gfb.framework.utils.MD5Utils;
import com.apass.zufang.domain.Response;
import com.apass.zufang.domain.entity.weex.BsdiffInfoEntity;
import com.apass.zufang.domain.entity.weex.BsdiffResponse;
import com.apass.zufang.domain.entity.weex.FileEntitis;
import com.apass.zufang.domain.vo.BsdiffParamVo;
import com.apass.zufang.service.weex.BsdiffinfoService;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaohai on 2018/3/6.
 */
@Controller
public class BsdiffAppColler {
    @Value("${zufang.image.uri}")
    private String appWebDomain;
    @Value("${nfs.rootPath}")
    private String rootPath;
    @Value("${nfs.bsdiff}")
    private String nfsBsdiffPath;

    private static final Logger LOGGER = LoggerFactory.getLogger(BsdiffAppColler.class);
    private static final String PATCHPATH = "/patchzip";

    @Autowired
    private BsdiffinfoService bsdiffinfoService;

    @RequestMapping(value = "/bsdiff/download")
    @ResponseBody
    public Response downLoad(@RequestBody(required=true) String jsonArr) throws IOException {
        LOGGER.info("bsdiff下载开始执行了,参数:{}", jsonArr);

        List<BsdiffResponse> responses = new ArrayList<>();
        try{
            List<BsdiffInfoEntity> bsdiffs = bsdiffinfoService.listAllNewest();

            //返回所有id对应的最新版本的数据
            if(StringUtils.isEmpty(jsonArr) || "{}".equals(jsonArr)){
                if(CollectionUtils.isEmpty(bsdiffs)){
                    throw new RuntimeException("数据库中没有对应数据，请先从后台上传");
                }

                for(BsdiffInfoEntity bs : bsdiffs){
                    BsdiffResponse bsr = getBsdiffResponse(bs,true,null);
                    responses.add(bsr);
                }
                return Response.success("下载成功",responses);
            }else{//返回对应增量更新的md5列表
                //记录所有最新版本号的id
                List<String> ids = new ArrayList<>();
                //解析json数组
                List<BsdiffParamVo> bspas = GsonUtils.convertList(jsonArr,new TypeToken<List<BsdiffParamVo>>(){});
                if(CollectionUtils.isEmpty(bspas)){
                    throw new RuntimeException("参数有误");
                }

                for (BsdiffParamVo bsvo: bspas) {
                    ids.add(bsvo.getId());
                    //判断ver是否是对应id下的最新ver，如果是：最新的merge文件，如果不是，返回对应patch包
                    //根据id查询对应最大版本号数据s
                    BsdiffInfoEntity entity = bsdiffinfoService.selectMaxBsdiffInfoById(bsvo.getId());
                    if(Integer.valueOf(bsvo.getVer()) > Integer.valueOf(entity.getBsdiffVer())){
                        LOGGER.error("数据有误，{}下的最大版本号为{}",entity.getLineId(),entity.getBsdiffVer());
                        throw new RuntimeException("数据有误,"+entity.getLineId()+"下的最大版本号为:"+entity.getBsdiffVer());
                    }else if(Integer.valueOf(bsvo.getVer()).equals(Integer.valueOf(entity.getBsdiffVer()))){
                        //返回增量包
                        BsdiffResponse bsr = getBsdiffResponse(entity,true,bsvo);
                        responses.add(bsr);
                    }else{
                        //返回增量包
                        BsdiffResponse bsr = getBsdiffResponse(entity,false,bsvo);
                        responses.add(bsr);
                    }
                }

                //未传id，但数据库存存在的情况
                List<BsdiffInfoEntity> otherBsdiff = new ArrayList<>();
                for(BsdiffInfoEntity bsdiff: bsdiffs){
                    if(!ids.contains(bsdiff.getLineId())){
                        otherBsdiff.add(bsdiff);
                    }
                }

                if(CollectionUtils.isNotEmpty(otherBsdiff)){
                    for(BsdiffInfoEntity bs: otherBsdiff){
                        BsdiffResponse bsr = getBsdiffResponse(bs,true,null);
                        responses.add(bsr);
                    }
                }
            }
        }catch (Exception e){
            LOGGER.error("下载失败"+e.getMessage(),e);
            return Response.fail("下载失败"+e.getMessage());
        }

        return Response.success("下载成功",responses);
    }

    /**
     * 封装到vo中，返回给app
     * @param bs
     * @param bo:true 返回merge文件，false 返回patch文件
     * @param bsvo：传入的参数
     * @return
     * @throws IOException
     */
    private BsdiffResponse getBsdiffResponse(BsdiffInfoEntity bs, boolean bo, BsdiffParamVo bsvo) throws IOException {
        BsdiffResponse bsr = new BsdiffResponse();
        bsr.setIfCompelUpdate(bs.getIfCompelUpdate());
        bsr.setBsdiffVer(bs.getBsdiffVer());
        bsr.setLineId(bs.getLineId());
        if(bo){
            //md5_patch为空
            bsr.setMd5_patch(null);

            //md5_merge
            String mergepath = bs.getMergeFilePath().substring(bs.getMergeFilePath().indexOf("static")+"static".length());
            LOGGER.info("md5_merge的路径：{}",rootPath+mergepath);
            String md5_merge = MD5Utils.getMD5(new File(rootPath+mergepath));
            bsr.setMd5_merge(md5_merge);

            bsr.setFileurl(bs.getMergeFilePath());
        }else{
            //md5_patch
            //先获取patch包目录
            String patch_path = rootPath+nfsBsdiffPath+PATCHPATH+"/"+bs.getLineId()+"/"+bs.getBsdiffVer();
            String patch_name = bs.getBsdiffVer()+"_"+bsvo.getVer()+".zip";
            LOGGER.info("md5_Patch的路径：{}",patch_path,patch_name);
            String md5_patch = MD5Utils.getMD5(new File(patch_path,patch_name));
            bsr.setMd5_patch(md5_patch);

            //md5_merge为空
            String mergepath = bs.getMergeFilePath().substring(bs.getMergeFilePath().indexOf("static")+"static".length());
            LOGGER.info("md5_merge的路径：{}",rootPath+mergepath);
            String md5_merge = MD5Utils.getMD5(new File(rootPath+mergepath));
            bsr.setMd5_merge(md5_merge);

            //patch文件url
            String patch_url = appWebDomain+nfsBsdiffPath+PATCHPATH+"/"+bs.getLineId()+"/"+bs.getBsdiffVer()+"/"+patch_name;

            bsr.setFileurl(patch_url);
        }

        //文件清单
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(bs.getFileListPath()))));
        StringBuffer sb = new StringBuffer();
        String str = null;
        while ((str=br.readLine())!=null){
            sb.append(str);
        }
        List<FileEntitis> list = GsonUtils.convertList(sb.toString(),  new com.google.gson.reflect.TypeToken<List<FileEntitis>>(){});

        bsr.setJsonList(list);
        return bsr;
    }

}
