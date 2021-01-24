package com.flipkart.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.flipkart.bean.Professor;
import com.flipkart.bean.Student;
import com.flipkart.constant.SQLQueriesConstant;
import com.flipkart.util.DBConnection;
import com.mysql.cj.protocol.Resultset;

/**
 * @author JEDI04
 * Lazy singleton class synchronized for multi-threading
 * interacts with professor related tables 
 *
 */
public class ProfessorDAOOperation implements ProfessorDAOInterface {
	private static Logger logger = Logger.getLogger(ProfessorDAOOperation.class);
	Connection con;
	PreparedStatement stmt;
	
	private static ProfessorDAOOperation instance = null;
	
	private ProfessorDAOOperation() {
		
	}
	
	synchronized public static ProfessorDAOOperation getInstance() {
		if (instance==null) {
			instance = new ProfessorDAOOperation();
		}
		return instance;
	}

	/**
	 * @author JEDI04
	 * Method creates and returns professor object using email ID from the database
	 * 
	 * @param email(String)
	 * @return Professor(object)
	 */
	public Professor getProfessorByEmail(String email) {
		Professor professor = new Professor();
		try {

			con = DBConnection.getConnection();
			String str = SQLQueriesConstant.GET_PROFESSOR_BY_EMAIL;
			stmt = con.prepareStatement(str);
			stmt.setString(1, email);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				professor = new Professor();
				professor.setUserId(rs.getInt(1));
				professor.setUserName(rs.getString(2));
				professor.setEmail(rs.getString("email"));
				professor.setRole(rs.getString("role"));
				professor.setDepartment(rs.getString(5));
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return professor;
	}

	/**
	 * @author JEDI04
	 * Method Displays list of courses alloted to the 
	 * professor after getting from the database
	 * 
	 * @param professorId(integer)
	 * @return none
	 */
	public void showCourses(int professorId) {
		try {
			con = DBConnection.getConnection();
			String str = SQLQueriesConstant.SHOW_COURSES_PROFESSOR_QUERY;
			stmt = con.prepareStatement(str);
			stmt.setInt(1, professorId);
			ResultSet rs = stmt.executeQuery();
			logger.info("===============================================");
			logger.info("ID		CourseName		Credits");
			while (rs.next()) {
				logger.info(rs.getInt(1) + "		" + rs.getString(2) + "		" + rs.getInt(3));
			}
			logger.info("================================================\n\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @author JEDI04
	 * Returns list of all student objects associated with the particular
	 * course id whose grade is Not Available from the database
	 * 
	 * @param courseId(integer)
	 * @return Arraylist(Student(Object))
	 */
	public ArrayList<Student> getEnrolledStudents(int courseId) {
		ArrayList<Student> al = new ArrayList<Student>();
		try {
			con = DBConnection.getConnection();
			String str = SQLQueriesConstant.GET_ENROLLED_STUDENTS_PROFESSOR_QUERY;
			stmt = con.prepareStatement(str);
			stmt.setInt(1, courseId);
			ResultSet rs = stmt.executeQuery();
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				Student st = new Student();
				st.setUserId(rs.getInt("id"));
				st.setUserName(rs.getString("name"));
				st.setEmail(rs.getString("email"));
				st.setBranch(rs.getString("branch"));
				al.add(st);
			}
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		return al;
	}

	/**
	 * @author JEDI04
	 * Updates grades of multiple student belonging
	 * to a particular courseId whose grades are currently
	 * not available
	 * 
	 * @param {ArrayList(Student(object)),grade(integer)}
	 * @return none
	 */
	public void setGrades(ArrayList<Student>toGrade,int courseId) {

		Scanner sc = new Scanner(System.in);
		try {
			con = DBConnection.getConnection();
			for(Student st : toGrade) {
				logger.info("Please Enter Grade for " + st.getUserName());
				String grd = sc.next();
				if(grd=="")
					grd = sc.next();
				String str = SQLQueriesConstant.SET_GRADES_PROFESSOR_QUERY;
				stmt = con.prepareStatement(str);
				stmt.setString(1, grd);
				stmt.setInt(2, st.getUserId());
				stmt.setInt(3, courseId);
				int isUpdated = stmt.executeUpdate();
				if(isUpdated > 0) {
					logger.info("Uploaded grade");
				}
				else {
					logger.info("Couldn't upload try again");
				}
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		return ;
	}
	
	/**
	 * @author JEDI04
	 * Updates grades of a single student belonging
	 * to a particular courseId
	 * 
	 * @param {courseId(integer),studentId(integer),grade(String)}
	 * @return boolean
	 */
	public boolean updateStudentGrades(int courseId,int studentId, String grades) {
		try {
			con = DBConnection.getConnection();
			String str = SQLQueriesConstant.UPDATE_GRADES_PROFESSOR_QUERY;
			stmt = con.prepareStatement(str);
			stmt.setString(1,grades);
			stmt.setInt(2,courseId);
			stmt.setInt(3,studentId);
			int status = stmt.executeUpdate();
			if (status>0) {
				return true;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * @author JEDI04
	 * Show Grades of all enrolled students associated with the 
	 * courseId
	 * 
	 * @param {ArrayList(Student(object)),integer}
	 * @return none
	 */
	public void showGrades(ArrayList<Student>enolledStudent,int courseId) {
		try {
			con = DBConnection.getConnection();
			logger.info("===================================");
			logger.info("UserId    UserName    Grade Obtained");
			for(Student st : enolledStudent) {
				String str = SQLQueriesConstant.SHOW_GRADES_PROFESSOR_QUERY;
				stmt = con.prepareStatement(str);
				stmt.setInt(1, st.getUserId());
				stmt.setInt(2, courseId);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					logger.info(st.getUserId() + "        " + st.getUserName() + "        " + rs.getString("grade"));
				}
			}
			logger.info("===================================");
		} catch (Exception e){
			e.printStackTrace();
		}
		return ;
	}
	
	
	
	/**
	 * @author JEDI04
	 * Returns list of all student objects associated with the particular
	 * course id from the database
	 * 
	 * @param courseId(integer)
	 * @return ArrayList(Student(object))
	 */
	public ArrayList<Student> getStudents(int courseId) {
		ArrayList<Student> al = new ArrayList<Student>();
		try {
			con = DBConnection.getConnection();
			String str = SQLQueriesConstant.GET_STUDENTS_PROFESSOR_QUERY;
			stmt = con.prepareStatement(str);
			stmt.setInt(1, courseId);
			ResultSet rs = stmt.executeQuery();
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				Student st = new Student();
				st.setUserId(rs.getInt("id"));
				st.setUserName(rs.getString("name"));
				st.setEmail(rs.getString("email"));
				st.setBranch(rs.getString("branch"));
				al.add(st);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return al;
	}
	
}
