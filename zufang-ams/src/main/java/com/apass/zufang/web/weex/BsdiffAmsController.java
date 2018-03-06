package com.apass.zufang.web.weex;

import com.apass.gfb.framework.security.toolkit.SpringSecurityUtils;
import com.apass.zufang.domain.Response;
import com.apass.zufang.domain.entity.weex.BsdiffInfoEntity;
import com.apass.zufang.domain.vo.BsdiffVo;
import com.apass.zufang.service.weex.BsdiffinfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * Created by xiaohai on 2018/3/6.
 */
@Controller
@RequestMapping("/application/system/param")
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
    @RequestMapping("/bsdiffUpload")
    public Response bsdiffUpload2(@ModelAttribute("bsdiffEntiry")BsdiffVo bsdiffVo) {
        try{
            LOG.info("bsdiff增量更新开始上传,参数 版本号:{},文件名:{}",bsdiffVo.getBsdiffVer(),bsdiffVo.getBsdiffFile().getOriginalFilename());
            BsdiffInfoEntity bsdiffInfoEntity = new BsdiffInfoEntity();
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
}
