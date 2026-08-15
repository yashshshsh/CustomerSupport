package com.aicustomersupport.demo.cs.service.interfac;

import com.aicustomersupport.demo.cs.dto.Response;
import com.aicustomersupport.demo.cs.model.TicketFeedback;

public interface ITicketFeedbackService {

    Response createFeedback(TicketFeedback feedback);

    Response getFeedback(Long id);

    Response getAllFeedback();

    Response getFeedbackByTicket(Long ticketId);

    Response updateFeedback(TicketFeedback feedback, Long id);

    Response deleteFeedback(Long id);
}