package org.Ermas.book.chap11.servlets;

import java.io.IOException;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.Ermas.book.chap11.dao.Member;
import org.Ermas.book.chap11.dao.MemberDao;
import org.Ermas.book.chap11.dao.MemberDaoImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@WebServlet("/members")
public class MemberListServlet extends HttpServlet {

	MemberDao memberDao = null;

	Logger logger = LogManager.getLogger();

	@Override
	public void init() throws ServletException {
		try {
			memberDao = new MemberDaoImpl();
		} catch (NamingException e) {
			logger.error(e.getMessage());
			throw new ServletException(e.getCause());
		}
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int page = 1;
		try { 
			page = Integer.parseInt(request.getParameter("page"));
		} catch (NumberFormatException e) {
			logger.error(e.getMessage());
		}

		final int COUNT = 100;
		
		int offset = (page - 1) * COUNT;

		List<Member> memberList = memberDao.selectAll(offset, COUNT);

		int totalCount = memberDao.countAll();

		request.setAttribute("totalCount", totalCount);
		request.setAttribute("members", memberList);
		request.getRequestDispatcher("/WEB-INF/jsp/members.jsp")
				.forward(request, response);
	}
}