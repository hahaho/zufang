package com.apass.zufang.web.appointment;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.apass.zufang.service.appointment.AppointmentJourneyService;
/**
 * 预约行程
 * @author Administrator
 *
 */
@Path("/appointment/appointmentJourneyController")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class AppointmentJourneyController {
	private static final Logger LOGGER  = LoggerFactory.getLogger(AppointmentJourneyController.class);
	@Autowired
	public AppointmentJourneyService appointmentJourneyService;
	/**
	 * 预约行程管理页面
	 * @return
	 */
	@GET
	@Path("/init")
    public String init() {
        return "appointment/appointmentJourney";
    }
}