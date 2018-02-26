package com.apass.zufang.web.appointment;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.apass.zufang.service.appointment.PhoneAppointmentService;
/**
 * 电话预约
 * @author Administrator
 *
 */
@Path("/appointment/apartmentController")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class PhoneAppointmentController {
	private static final Logger LOGGER  = LoggerFactory.getLogger(PhoneAppointmentController.class);
	@Autowired
	public PhoneAppointmentService phoneAppointmentService;
	/**
	 * 电话预约管理页面
	 * @return
	 */
	@GET
	@Path("/init")
    public String init() {
        return "appointment/phoneAppointment";
    }
}