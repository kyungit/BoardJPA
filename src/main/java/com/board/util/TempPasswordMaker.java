package com.board.util;

import java.util.Random;
//임시 패스워드 생성
public class TempPasswordMaker {
	
		
		public String tempPasswordMaker() {
			//숫자 + 영문대소문자 7자리 임시패스워드 생성
			StringBuffer tempPW = new StringBuffer();
			Random rnd = new Random();
			for (int i = 0; i < 7; i++) {
			    int rIndex = rnd.nextInt(3);
			    switch (rIndex) {
			    case 0:
			        // a-z : 아스키코드 97~122
			    	tempPW.append((char) ((int) (rnd.nextInt(26)) + 97));
			        break;
			    case 1:
			        // A-Z : 아스키코드 65~122
			    	tempPW.append((char) ((int) (rnd.nextInt(26)) + 65));
			        break;
			    case 2:
			        // 0-9
			    	tempPW.append((rnd.nextInt(10)));
			        break;
			    }
			}		
			return tempPW.toString();	
		}
}
