package com.apass.zufang.web.rbac;

import com.apass.zufang.domain.Response;
import com.apass.gfb.framework.security.domains.SecurityAccordion;
import com.apass.gfb.framework.security.domains.SecurityAccordionTree;
import com.apass.gfb.framework.security.domains.SecurityAuthentication;
import com.apass.gfb.framework.security.domains.SecurityMenus;
import com.apass.gfb.framework.security.toolkit.SpringSecurityUtils;
import com.apass.gfb.framework.security.userdetails.ListeningCustomSecurityUserDetails;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

/**
 * 
 * @description Get Sping Security Login UserInfo 
 *
 * @author lixining
 * @version $Id: SecurityInfoController.java, v 0.1 2015年9月9日 下午1:54:56 lixining Exp $
 */

@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class SecurityInfoController {
    /**
     * LOG
     */
    private static final Logger LOG = LoggerFactory.getLogger(SecurityInfoController.class);

    /**
     * Get Simple Authentication Info
     */
    @POST
    @Path(value = "/listeningboot/security/sauthinfo")
    public SecurityAuthentication getSecurityAuthentication() {
        return SpringSecurityUtils.getSecurityAuthentication();
    }

    /**
     * Get System Login Menus
     * 
     * @return List<SecurityMenusModel>
     */
    @POST
    @Path("/application/security/loginmenus")
    public Response handleSecurityLoginMenus() {
        try {
            List<SecurityAccordionTree> resultList = Lists.newArrayList();
            Object principal = SpringSecurityUtils.getAuthentication().getPrincipal();
            if (principal == null || !(principal instanceof ListeningCustomSecurityUserDetails)) {
                return Response.fail("加载登陆菜单失败,请联系管理员");
            }
            ListeningCustomSecurityUserDetails details = (ListeningCustomSecurityUserDetails) principal;
            SecurityMenus securityMenus = details.getSecurityMenus();
            treatSecurityMenus(securityMenus, resultList);
            return Response.success("success", resultList);
        } catch (Exception e) {
            LOG.error("get login menu fail", e);
            return Response.fail("加载登陆菜单失败,请联系管理员");
        }
    }

    /**
     * Convert SecurityMenus To List<SecurityMenusModel>
     * 
     * @param securityMenus
     * @param resultList
     */
    public void treatSecurityMenus(SecurityMenus securityMenus, List<SecurityAccordionTree> resultList) {
        List<SecurityAccordion> accordionList = securityMenus.getSecurityAccordionList();
        Map<String, List<SecurityAccordionTree>> menuMap = securityMenus.getAccordionTreeListMap();
        if (CollectionUtils.isEmpty(accordionList)) {
            return;
        }
        for (SecurityAccordion accordion : accordionList) {
            SecurityAccordionTree accordionMenu = new SecurityAccordionTree();
            accordionMenu.setId(accordion.getId());
            accordionMenu.setText(accordion.getText());
            if (menuMap == null || !menuMap.containsKey(accordion.getId())) {
                resultList.add(accordionMenu);
                continue;
            }
            if (!CollectionUtils.isEmpty(menuMap.get(accordion.getId()))) {
                accordionMenu.setChildren(menuMap.get(accordion.getId()));
                resultList.add(accordionMenu);
            }
        }
    }
}
