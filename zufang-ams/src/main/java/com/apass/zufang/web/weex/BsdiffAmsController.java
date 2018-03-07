package com.apass.zufang.web.weex;

import com.apass.gfb.framework.security.toolkit.SpringSecurityUtils;
import com.apass.gfb.framework.utils.CommonUtils;
import com.apass.gfb.framework.utils.HttpWebUtils;
import com.apass.zufang.domain.Response;
import com.apass.zufang.domain.entity.weex.BsdiffInfoEntity;
import com.apass.zufang.domain.vo.BsdiffVo;
import com.apass.zufang.service.weex.BsdiffinfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Created by xiaohai on 2018/3/6.
 */
@Controller
@RequestMapping("/noauth/bsdiff") //TODO
public class BsdiffAmsController {
    /**
     * 日志
     */
    private static final Logger LOG = LoggerFactory.getLogger(BsdiffAmsController.class);

    @Autowired
    private BsdiffinfoService bsdiffinfoService;
    /**
     * 增量更新升级版
     * @param bsdiffVo
     * @return
     */
    @ResponseBody
    @RequestMapping("/upload")
    public Response bsdiffUpload2(@ModelAttribute("bsdiffEntiry")BsdiffVo bsdiffVo) {
        BsdiffInfoEntity bsdiffInfoEntity = new BsdiffInfoEntity();
        try{
            LOG.info("bsdiff增量更新开始上传,参数 版本号:{},文件名:{}",bsdiffVo.getBsdiffVer(),bsdiffVo.getBsdiffFile().getOriginalFilename());
            bsdiffInfoEntity.setCreatedTime(new Date());
            bsdiffInfoEntity.setCreateUser(SpringSecurityUtils.getCurrentUser());
            bsdiffInfoEntity.setUpdatedTime(new Date());
            bsdiffInfoEntity.setUpdateUser(SpringSecurityUtils.getCurrentUser());
            bsdiffInfoEntity.setIfCompelUpdate(bsdiffVo.getIfCompelUpdate());
            bsdiffinfoService.bsdiffUpload(bsdiffVo,bsdiffInfoEntity);
        }catch (Exception e){
            LOG.error("增量添加上传失败",e);
            return Response.fail(e.getMessage());
        }
        return Response.success("增量添加上传成功");
    }

    @ResponseBody
    @RequestMapping("/list")
    public Response pageList(HttpServletRequest request) {
        try {
            List<BsdiffInfoEntity> list = bsdiffinfoService.listAll();
            return Response.success("查询成功bsdiff列表成功",list);
        }catch (Exception e){
            LOG.error("查询成功bsdiff列表失败",e);
            return Response.fail("查询成功bsdiff列表失败");
        }
    }
}
