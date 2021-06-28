package pkg;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class Database {
	public static void main(String args[]) throws SQLException, IOException {
		try{
			Class.forName("com.mysql.jdbc.Driver");
		} catch(ClassNotFoundException e){
			System.out.println("Could not load the driver");
		}
		
		Connection conn = DriverManager.getConnection("jdbc:mysql://db.hufs.ac.kr:3306/s201401793DB", "s201401793", "01085771378");
		String query;
		PreparedStatement p;
		ResultSet r;
		
		Scanner in = new Scanner(System.in);
		
		System.out.println("Welcome to US!!");
		System.out.println("");
		
		while(true){
			query = "SELECT Dname, DNumber FROM DEPARTMENT";
			p = conn.prepareStatement(query);
			r = p.executeQuery();
		
			System.out.println("----------------------");
			System.out.println("dnumber     dname");
			while(r.next()){  // 위의 SQL QUERY를 실행한 결과를 모두 가져온 후 출력함
				String dname = r.getString(1);  // dnum: 데이터베이스 내의 부서명
				int dnumber = r.getInt(2);  // dnumber: 데이터베이스 내의 부서 번호
			
				System.out.println(dnumber + "     " + dname);
			}
			
			query = "SELECT Salary FROM EMPLOYEE WHERE Dno = ?";
			p = conn.prepareStatement(query);
			
			String dno;  // dno: 입력받을 부서 번호
			while(true){
				System.out.print("Enter the department number: ");
				dno = in.nextLine();
				
				if(isInteger(dno)) break;  // 양의 정수를 입력하지 않으면 에러 메시지를 띄우고 다시 입력받음
				
				System.out.println("Error: Only Positive Integer is allowed");
			}
			
			ArrayList<Integer> salaries = new ArrayList<Integer>();  // salaries: 직원들의 Salary 값들을 모두 저장하는 List
			
			p.clearParameters();
			p.setString(1, dno);  // ? 값을 입력받은 부서 번호로 replace함
			r = p.executeQuery();
			while(r.next()){  // 직원들의 Salary 값들을 모두 저장
				int salary = r.getInt(1);
				
				salaries.add(salary);
			}
			
			int arr[] = new int[salaries.size()];
			for(int i=0; i<salaries.size(); i++)
				arr[i] = salaries.get(i);
			
			int min = minValue(arr);  // 중간 값을 구함
			
			if(min == -1)  // min 값이 -1이라는 것은 EMPLOYEE 테이블에 없는 부서 번호를 입력했다는 뜻
				System.out.println("No employees found by dnumber " + dno);
			else
				System.out.println("Median of salaries of dnumber " + dno + " is " + min);
			
			String re;  // re: 프로그램 재실행 여부 (y or n)
			while(true){
				System.out.print("Do you want to continue (y/n)? ");
				re = in.nextLine();
				
				if(re.equals("y") || re.equals("n")) break;  // y 또는 n을 입력하지 않으면 에러 메시지를 띄우고 다시 입력받음
				
				System.out.println("Error: Only Character - y or n");
			}
			
			if(re.equals("n")) break;  // n을 입력했을 경우 프로그램을 종료함
		}
		
		in.close();
		p.close();
		conn.close();  // n을 입력했을 경우 모두 close한 다음에 종료함
		
		System.out.println("");
		System.out.println("Bye~");
	}
	
	public static int minValue(int[] arr){  // 배열 내에서 중간값을 구하는 함수
		int len = arr.length;  // len: 배열의 길이
		
		for(int i=0; i<len; i++){
			int cnt = 0;  // cnt: target 값보다 더 큰 값이 몇개인지 저장
			int target = arr[i];  // target: 배열값 중 하나
			
			for(int j=0; j<len; j++){  // target 값보다 더 큰 값이 몇개인지 계산함
				if(target < arr[j])
					cnt++;
			}
			
			int mcount = (int)((len+1)/2);  // mcount: 배열 내의 중간 값은 보다 더 큰 값을 몇 개 가지고 있어야 하는지를 저장
			if(len%2 == 1)  // 홀수일 경우 -1
				mcount--;
			
			if(cnt == mcount)  // 해당 숫자가 중간값인지 판단
				return target;
		}
		
		return -1;  // some error
	}
	
	public static boolean isInteger(String strnum){  // 양의 정수인지 판단하는 함수
		if(strnum.length() == 1 && strnum.charAt(0) == '0')  // 0이면 false
			return false;
		
		for(int c=0; c<strnum.length(); c++){
			if(!(strnum.charAt(c) >= 48 && strnum.charAt(c) <= 57))
				return false;    
		}
		
		return true;	
	}
}