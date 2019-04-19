package org.Ermas.book.chap11.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JdbcTemplate {

	static final Logger logger = LogManager.getLogger();

	DataSource dataSource;

	public JdbcTemplate(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public <T> List<T> queryForList(String query, Object[] params,
			RowMapper<T> rowMapper) throws DaoException {
		try (Connection con = dataSource.getConnection();
				PreparedStatement ps = con.prepareStatement(query)) {
			setParams(ps, params);
			List<T> list = new ArrayList<>();
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				list.add(rowMapper.mapRow(rs));
			rs.close();
			return list;
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new DaoException(e);
		}
	}

	public <T> T queryForObject(String query, Object[] params,
			RowMapper<T> rowMapper) throws DaoException {
		try (Connection con = dataSource.getConnection();
				PreparedStatement ps = con.prepareStatement(query)) {
			setParams(ps, params);
			ResultSet rs = ps.executeQuery();
			T t = null;
			if (rs.next())
				t = rowMapper.mapRow(rs);
			rs.close();
			return t;
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new DaoException(e);
		}
	}

	public int update(String query, Object... params) throws DaoException {
		try (Connection con = dataSource.getConnection();
				PreparedStatement ps = con.prepareStatement(query)) {
			setParams(ps, params);
			return ps.executeUpdate();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new DaoException(e);
		}
	}

	private void setParams(PreparedStatement ps, Object[] params)
			throws SQLException {
		if (params != null) {
			for (int i = 0; i < params.length; i++)
				ps.setObject(i + 1, params[i]);
		}
	}
}