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
			while(r.next()){  // ���� SQL QUERY�� ������ ����� ��� ������ �� �����
				String dname = r.getString(1);  // dnum: �����ͺ��̽� ���� �μ���
				int dnumber = r.getInt(2);  // dnumber: �����ͺ��̽� ���� �μ� ��ȣ
			
				System.out.println(dnumber + "     " + dname);
			}
			
			query = "SELECT Salary FROM EMPLOYEE WHERE Dno = ?";
			p = conn.prepareStatement(query);
			
			String dno;  // dno: �Է¹��� �μ� ��ȣ
			while(true){
				System.out.print("Enter the department number: ");
				dno = in.nextLine();
				
				if(isInteger(dno)) break;  // ���� ������ �Է����� ������ ���� �޽����� ���� �ٽ� �Է¹���
				
				System.out.println("Error: Only Positive Integer is allowed");
			}
			
			ArrayList<Integer> salaries = new ArrayList<Integer>();  // salaries: �������� Salary ������ ��� �����ϴ� List
			
			p.clearParameters();
			p.setString(1, dno);  // ? ���� �Է¹��� �μ� ��ȣ�� replace��
			r = p.executeQuery();
			while(r.next()){  // �������� Salary ������ ��� ����
				int salary = r.getInt(1);
				
				salaries.add(salary);
			}
			
			int arr[] = new int[salaries.size()];
			for(int i=0; i<salaries.size(); i++)
				arr[i] = salaries.get(i);
			
			int min = minValue(arr);  // �߰� ���� ����
			
			if(min == -1)  // min ���� -1�̶�� ���� EMPLOYEE ���̺� ���� �μ� ��ȣ�� �Է��ߴٴ� ��
				System.out.println("No employees found by dnumber " + dno);
			else
				System.out.println("Median of salaries of dnumber " + dno + " is " + min);
			
			String re;  // re: ���α׷� ����� ���� (y or n)
			while(true){
				System.out.print("Do you want to continue (y/n)? ");
				re = in.nextLine();
				
				if(re.equals("y") || re.equals("n")) break;  // y �Ǵ� n�� �Է����� ������ ���� �޽����� ���� �ٽ� �Է¹���
				
				System.out.println("Error: Only Character - y or n");
			}
			
			if(re.equals("n")) break;  // n�� �Է����� ��� ���α׷��� ������
		}
		
		in.close();
		p.close();
		conn.close();  // n�� �Է����� ��� ��� close�� ������ ������
		
		System.out.println("");
		System.out.println("Bye~");
	}
	
	public static int minValue(int[] arr){  // �迭 ������ �߰����� ���ϴ� �Լ�
		int len = arr.length;  // len: �迭�� ����
		
		for(int i=0; i<len; i++){
			int cnt = 0;  // cnt: target ������ �� ū ���� ����� ����
			int target = arr[i];  // target: �迭�� �� �ϳ�
			
			for(int j=0; j<len; j++){  // target ������ �� ū ���� ����� �����
				if(target < arr[j])
					cnt++;
			}
			
			int mcount = (int)((len+1)/2);  // mcount: �迭 ���� �߰� ���� ���� �� ū ���� �� �� ������ �־�� �ϴ����� ����
			if(len%2 == 1)  // Ȧ���� ��� -1
				mcount--;
			
			if(cnt == mcount)  // �ش� ���ڰ� �߰������� �Ǵ�
				return target;
		}
		
		return -1;  // some error
	}
	
	public static boolean isInteger(String strnum){  // ���� �������� �Ǵ��ϴ� �Լ�
		if(strnum.length() == 1 && strnum.charAt(0) == '0')  // 0�̸� false
			return false;
		
		for(int c=0; c<strnum.length(); c++){
			if(!(strnum.charAt(c) >= 48 && strnum.charAt(c) <= 57))
				return false;    
		}
		
		return true;	
	}
}