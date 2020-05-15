package com.isu.erp.eaccount.common;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

import java.util.HashMap;

/**
 * @auther : yukpan
 * @date : 2019.06.18
 * @explain : 프로토콜을 정의
 *            GET > @RequestParam - @Query
 *            POST > @RequestParam - @Field , @FieldMap
 *            POST > @@RequestBody -  @Body
 **/
public interface RetrofitService {

    /***
     * @author : hmyuk
     * @since : 2019.6.18
     * @param : groupCode (그룹코드)
     * @explain 모바일에서 작성된 groupCode를 가지고 DB의 groupCode를 조회한다.
     * **/
    @POST("authGrpCode")
    Call<JsonObject> getAuthGrpCode(@Body HashMap<String,Object> param);

    /***
     * @author hmyuk
     * @since 2019.6.18
     * @param  : mobileGroupId (그룹코드ID)
     * @explain 로그인시 받은 그룹코드ID를 가지고 해당 그룹에 소속된 회사의 리스트를 가져온다.
     * @return
     *
     *  result : {
     *  			"resultYn" : "Y",
     *  			"groupData" : {
     *  				"tenantId" : "",
     *  				"tenantKey" : "",
     *  				"companyName" : "",
     *  				"gwPrefix" : "",
     *  				"bukrs" : ""
     *  			}
     *  		 }
     *  ### Result Data Explain ###
     *  resultYn : 성공여부
     *  tenantId : 테넌트 ID
     *  tenantKey : 테넌트 KEY (abx)
     *  companyName : 회사명
     *  gwPrefix : 그룹 prefix (ISU_CH)
     *  bukrs : 회사코드
     * **/
    @POST("companyList")
    Call<JsonObject> getCompanyList(@Body HashMap<String,Object> param);

    /***
     * @author hmyuk
     * @since 2019.6.18
     * @param {
     * 			"tenantId" : "",
     * 			"id" : "",
     * 			"password" : "",
     * 			"tenantKey" : "",
     * 			"gwPrefix" : "",
     * 			"bukrs" : ""
     * 		  }
     * @explain 로그인시 받은 그룹코드ID를 가지고 해당 그룹에 소속된 회사의 리스트를 가져온다.
     * @return
     *
     * 	1. 그룹웨어 인증 (URL : http://gw.isudev.com/CoviWeb/api/session.aspx?id=:id&password=:password)
     *  2. SAP 로그인 요청 ( FID : ZFI_ED_M_010 )
     *
     *  result : {
     *  			"resultYn" : "Y",
     *  			"userData" : {
     *  				"tenantId" : "",
     *  				"tenantKey" : "",
     *  				"accessToken" : "",
     *  				"gwPrefix" : "",
     *  				"password" : ""
     *  			},
     *  			"columnDefine" : [{
     *	  					"title": "총금액",
     *						"columnName": "DMBTR",
     *						"type": "textField",
     *						"description": "총금액을 작성해주세요.",
     *						"masterVisible": "false",
     *						"detailVisible": "true"
     *  			}]
     *  		 }
     *  ### Result Data Explain ###
     *  resultYn : 성공여부
     *  tenantId : 테넌트 ID
     *  tenantKey : 테넌트 KEY (abx)
     *  companyName : 회사명
     *  gwPrefix : 그룹 prefix (ISU_CH)
     *  password : 비밀번호
     *  title : 시트에 보일 타이틀명
     *  columnName : SAP 컬럼과 매핑 시킬 컬럼명
     *  type : 타입 (label / textField / date / search / select)
     *  description : 설명 (placeholder)
     *  masterVisible : master 사용여부 (iOS 기준 사용하지 않음)
     *  detailVisible : 상세화면에서 해당 필드를 보여줄지 여부
     * **/
    @POST("login")
    Call<JsonObject> getLogin(@Body HashMap<String,Object> param);

    /***
     * @author : hmyuk
     * @since : 2019.6.18
     * @param {
     * 			"I_BUKRS" : "",
     * 			"I_PERNR" : "",
     * 			"I_SDATE" : "",
     * 			"I_EDATE" : ""
     * 		  }
     * @explain 메인화면 건수 조회
     * @return
     *  result : {
     *  			"resultYn" : "Y",
     *  			"statusCntData" : {
     *  				"O_GUNSU1" : "",
     *  				"O_GUNSU2" : "",
     *  				"O_GUNSU3" : "",
     *  				"O_GUNSU4" : "",
     *  				"O_GUNSU5" : ""
     *  			}
     *  		 }
     *  ### Result Data Explain ###
     *  I_BUKRS : 회사코드
     *  I_PERNR : 사원번호
     *  I_SDATE : 시작일
     *  I_EDATE : 종료일
     *  O_GUNSU1 : 법인카드 미처리 건 수
     *  O_GUNSU2 : 세금계산서 미처리 건 수
     *  O_GUNSU3 : 결재 미상신 건수
     *  O_GUNSU4 : 결재 진행중 건수
     *  O_GUNSU5 : 결재 반려 건수
     * **/
    @POST("ZFI_ED_M_001")
    Call<JsonObject> getZFI_ED_M_001(@Body HashMap<String,Object> param);

    /***
     * @author hmyuk
     * @since 2019.6.18
     * @param {
     * 			"BUKRS" : "",
     * 			"PERNR" : "",
     * 			"tenantId" : "",
     * 			"tenantKey" : "",
     * 			"I_BUKRS" : "",
     * 			"I_APPDT_F" : "",
     * 			"I_APPDT_T" : "",
     * 			"I_CARDNO" : "",
     * 			"I_KOSTL" : "",
     * 			"I_PERNR" : ""
     * 		  }
     * @explain 법인카드 내역조회
     * @return
     *  result : {
     *  			"resultYn" : "Y",
     *  			"cardData" : [{
     *  				...
     *  			}],
     *  			"codeData" : {
     *  			}
     *  		 }
     *  ### Result Data Explain ###
     *  BUKRS : 회사코드
     *  PERNR : 사원번호
     *  tenantId : 테넌트 ID
     *  tenantKey : 테넌트 KEY (abx)
     *  I_BUKRS : 회사코드
     *  I_APPDT_F : 승인일자 fr
     *  I_APPDT_T : 승인일자 to
     *  I_CARDNO : 카드번호
     *  I_KOSTL : 코스트센터
     *  I_PERNR : 사번
     *  cardData : 법인카드 데이터
     *  codeData : select 에 맞는 코드들의 리스트
     * **/
    @POST("ZFI_ED_M_002")
    Call<JsonObject> getZFI_ED_M_002(@Body HashMap<String,Object> param);

    /***
     * @author hmyuk
     * @since 2019.6.18
     * @param {
     * 			"BUKRS" : "",
     * 			"PERNR" : "",
     * 			"tenantId" : "",
     * 			"tenantKey" : "",
     * 			"I_EVGBN" : "",
     * 			"I_PERNR" : "",
     * 			"data" : [...]
     * 		  }
     * @explain 법인카드전표생성
     * @return
     *  result : {
     *  			"resultYn" : "Y",
     *  			"message" : ""
     *  		 }
     *  ### Result Data Explain ###
     *  BUKRS : 회사코드
     *  PERNR : 사원번호
     *  tenantId : 테넌트 ID
     *  tenantKey : 테넌트 KEY (abx)
     *  data : T_001 에 들어갈 데이터
     * **/
    @POST("ZFI_ED_M_003")
    Call<JsonObject> getZFI_ED_M_003(@Body HashMap<String,Object> param);

    /***
     * @author hmyuk
     * @since 2019.6.18
     * @param {
     * 			"BUKRS" : "",
     * 			"PERNR" : "",
     * 			"tenantId" : "",
     * 			"tenantKey" : ""
     * 		  }
     * @explain 실물증빙 전표등록화면에 사용될 코드 데이터를 가져온다.
     * @return
     *  result : {
     *  			"resultYn" : "Y",
     *  			"message" : ""
     *  		 }
     *  ### Result Data Explain ###
     *  BUKRS : 회사코드
     *  PERNR : 사원번호
     *  tenantId : 테넌트 ID
     *  tenantKey : 테넌트 KEY (abx)
     * **/
    @POST("ZFI_ED_M_004")
    Call<JsonObject> getZFI_ED_M_004(@Body HashMap<String,Object> param);


    /***
     * @author hmyuk
     * @since 2019.6.18
     * @param {
     * 			"BUKRS" : "",
     * 			"PERNR" : "",
     * 			"tenantId" : "",
     * 			"tenantKey" : "",
     * 			"I_BUKRS" : "",
     * 			"I_BUDAT_F" : "",
     * 			"I_BUDAT_T" : "",
     * 			"I_CARDNO" : "",
     * 			"I_KOSTL" : "",
     * 			"I_PERNR" : ""
     * 		  }
     * @explain 결재요청 조회
     * @return
     *  result : {
     *  			"resultYn" : "Y",
     *  			"cardData" : [{
     *  				...
     *  			}],
     *  			"codeData" : {
     *  			}
     *  		 }
     *  ### Result Data Explain ###
     *  BUKRS : 회사코드
     *  PERNR : 사원번호
     *  tenantId : 테넌트 ID
     *  tenantKey : 테넌트 KEY (abx)
     *  I_BUKRS : 회사코드
     *  I_APPDT_F : 증빙일 fr
     *  I_APPDT_T : 증빙일 to
     *  I_CARDNO : 카드번호
     *  I_KOSTL : 코스트센터
     *  I_PERNR : 사번
     *  cardData : 법인카드 데이터
     * **/
    @POST("ZFI_ED_M_005")
    Call<JsonObject> getZFI_ED_M_005(@Body HashMap<String,Object> param);

    /***
     * @author hmyuk
     * @since 2019.6.18
     * @param {
     * 			"BUKRS" : "",
     * 			"PERNR" : "",
     * 			"tenantId" : "",
     * 			"tenantKey" : "",
     * 			"I_BUKRS" : "",
     * 			"I_APPDT_F" : "",
     * 			"I_APPDT_T" : "",
     * 			"I_CARDNO" : "",
     * 			"I_KOSTL" : "",
     * 			"I_PERNR" : "",
     * 			"I_APPR" : ""
     * 		  }
     * @explain 전표처리 내역조회
     * @return
     *  result : {
     *  			"resultYn" : "Y",
     *  			"cardData" : [{
     *  				...
     *  			}]
     *  		 }
     *  ### Result Data Explain ###
     *  BUKRS : 회사코드
     *  PERNR : 사원번호
     *  tenantId : 테넌트 ID
     *  tenantKey : 테넌트 KEY (abx)
     *  I_BUKRS : 회사코드
     *  I_APPDT_F : 승인일자 fr
     *  I_APPDT_T : 승인일자 to
     *  I_CARDNO : 카드번호
     *  I_KOSTL : 코스트센터
     *  I_PERNR : 사번
     *  I_APPR : 승인요청 ( G : 승인, Y : 승인요청, R : 반려 )
     *  cardData : 법인카드 데이터
     * **/
    @POST("ZFI_ED_M_008")
    Call<JsonObject> getZFI_ED_M_008(@Body HashMap<String,Object> param);

    /***
     * @author hmyuk
     * @since 2019.6.18
     * @param {
     * 			"BUKRS" : "",
     * 			"PERNR" : "",
     * 			"tenantId" : "",
     * 			"tenantKey" : "",
     * 			"I_BUKRS" : "",
     * 			"I_BLDAT_F" : "",
     * 			"I_BLDAT_T" : "",
     * 			"I_CARDNO" : "",
     * 			"I_KOSTL" : "",
     * 			"I_PERNR" : "",
     * 			"I_APPR" : "",
     * 			"I_EVGBN" : ""
     * 		  }
     * @explain 실물증빙 내역 조회
     * @return
     *  result : {
     *  			"resultYn" : "Y",
     *  			"cardData" : [{
     *  				...
     *  			}]
     *  		 }
     *  ### Result Data Explain ###
     *  BUKRS : 회사코드
     *  PERNR : 사원번호
     *  tenantId : 테넌트 ID
     *  tenantKey : 테넌트 KEY (abx)
     *  I_BUKRS : 회사코드
     *  I_APPDT_F : 승인일자 fr
     *  I_APPDT_T : 승인일자 to
     *  I_CARDNO : 카드번호
     *  I_KOSTL : 코스트센터
     *  I_PERNR : 사번
     *  I_APPR : 승인요청 ( G : 승인, Y : 승인요청, R : 반려 )
     *  I_EVGBN : 증빙구분
     *  data  : 데이터
     * **/
    @POST("ZFI_ED_M_009")
    Call<JsonObject> getZFI_ED_M_009(@Body HashMap<String,Object> param);


    /***
     * @author hmyuk
     * @since 2019.6.18
     * @param {
     * 			"BUKRS" : "",
     * 			"PERNR" : "",
     * 			"tenantId" : "",
     * 			"tenantKey" : "",
     * 			"I_ZGUBUN1" : "",
     * 			"I_ZPROG" : ""
     * 		  }
     * @explain 구분값 변경시 호출되는 endpoint  ( ZFI_ED_M_F4_008 , ZFI_ED_M_F4_009 )
     * @return
     *  result : {
     *  			"resultYn" : "Y",
     *  			"data" : {
     *  				"ZGUBUN2" : [{
     *  				}],
     *  				"MWSKZ" : [{
     *  				}]
     *  			}
     *  		 }
     *  ### Result Data Explain ###
     *  BUKRS : 회사코드
     *  PERNR : 사원번호
     *  tenantId : 테넌트 ID
     *  tenantKey : 테넌트 KEY (abx)
     *  I_ZGUBUN1 : 대계정코드
     *  I_ZPROG : 프로그램 ID
     *  ZGUBUN2 : 대계정
     *  MWSKZ : 세금코드
     *  data  : 데이터
     * **/
    @POST("changeGubun")
    Call<JsonObject> getChangeGubun(@Body HashMap<String,Object> param);

    /***
     * @author hmyuk
     * @since 2019.6.18
     * @param {
     * 			"BUKRS" : "",
     * 			"PERNR" : "",
     * 			"tenantId" : "",
     * 			"tenantKey" : "",
     * 			"I_KOSTL" : "",
     * 			"I_PERNR" : ""
     * 		  }
     * @explain 부서 F4
     * @return
     *  result : {
     *  			"resultYn" : "Y",
     *  			"data" : [{...}]
     *  		 }
     *  ### Result Data Explain ###
     *  BUKRS : 회사코드
     *  PERNR : 사원번호
     *  tenantId : 테넌트 ID
     *  tenantKey : 테넌트 KEY (abx)
     *  I_KOSTL : 부서
     *  I_PERNR : 사원번호
     *  data : 데이터
     * **/
    @POST("popup/ZFI_ED_M_F4_003")
    Call<JsonObject> getZFI_ED_M_F4_003(@Body HashMap<String,Object> param);

    /***
     * @author hmyuk
     * @since 2019.6.18
     * @param {
     * 			"BUKRS" : "",
     * 			"PERNR" : "",
     * 			"tenantId" : "",
     * 			"tenantKey" : "",
     * 			"I_KOSTL" : "",
     * 			"I_PERNR" : "",
     * 			"I_ZPROG" : "",
     * 			"searchKey" : ""
     * 		  }
     * @explain 계정 F4
     * @return
     *  result : {
     *  			"resultYn" : "Y",
     *  			"data" : [{...}]
     *  		 }
     *  ### Result Data Explain ###
     *  BUKRS : 회사코드
     *  PERNR : 사원번호
     *  tenantId : 테넌트 ID
     *  tenantKey : 테넌트 KEY (abx)
     *  I_KOSTL : 부서
     *  I_ZPROG : 프로그램 ID
     *  searchKey : (=I_HKONT)
     *  I_PERNR : 사원번호
     *  data : 데이터
     * **/
    @POST("popup/ZFI_ED_M_F4_004")
    Call<JsonObject> getZFI_ED_M_F4_004(@Body HashMap<String,Object> param);

    /***
     * @author hmyuk
     * @since 2019.6.18
     * @param {
     * 			"BUKRS" : "",
     * 			"PERNR" : "",
     * 			"tenantId" : "",
     * 			"tenantKey" : "",
     * 			"I_KOSTL" : "",
     * 			"I_PERNR" : "",
     * 			"searchKey" : ""
     * 		  }
     * @explain 프로젝트 F4
     * @return
     *  result : {
     *  			"resultYn" : "Y",
     *  			"data" : [{...}]
     *  		 }
     *  ### Result Data Explain ###
     *  BUKRS : 회사코드
     *  PERNR : 사원번호
     *  tenantId : 테넌트 ID
     *  tenantKey : 테넌트 KEY (abx)
     *  I_KOSTL : 부서
     *  searchKey : (=)
     *  I_PERNR : 사원번호
     *  data : 데이터
     * **/
    @POST("popup/ZFI_ED_M_F4_006")
    Call<JsonObject> getZFI_ED_M_F4_006(@Body HashMap<String,Object> param);

    /***
     * @author hmyuk
     * @since 2019.6.18
     * @param {
     * 			"BUKRS" : "",
     * 			"PERNR" : "",
     * 			"tenantId" : "",
     * 			"tenantKey" : "",
     * 			"I_LIFNR" : "",
     * 			"searchKey" : ""
     * 		  }
     * @explain 구매처 F4
     * @return
     *  result : {
     *  			"resultYn" : "Y",
     *  			"data" : [{...}]
     *  		 }
     *  ### Result Data Explain ###
     *  BUKRS : 회사코드
     *  PERNR : 사원번호
     *  tenantId : 테넌트 ID
     *  tenantKey : 테넌트 KEY (abx)
     *  I_LIFNR : 구매처코드
     *  searchKey : (=)
     *  data : 데이터
     * **/
    @POST("popup/ZFI_ED_M_F4_012")
    Call<JsonObject> getZFI_ED_M_F4_012(@Body HashMap<String,Object> param);

    /***
     * @author hmyuk
     * @since 2019.6.18
     * @param {
     * 			"tenantId" : "",
     * 			"data" : "",
     * 		  }
     * @explain 결재요청 그룹삭제
     * @return
     *  result : {
     *  			"resultYn" : "Y",
     *  			"message" : ""
     *  		 }
     *  ### Result Data Explain ###
     *  BUKRS : 회사코드
     *  PERNR : 사원번호
     *  tenantId : 테넌트 ID
     *  tenantKey : 테넌트 KEY (abx)
     *  data : 데이터
     * **/
    @POST("popup/ZFI_ED_014_2")
    Call<JsonObject> getZFI_ED_014_2(@Body HashMap<String,Object> param);

    /***
     * @author hmyuk
     * @since 2019.6.18
     * @param {
     * 			"tenantId" : "",
     * 			"data" : "",
     * 		  }
     * @explain 결재요청 그룹생성
     * @return
     *  result : {
     *  			"resultYn" : "Y",
     *  			"message" : ""
     *  		 }
     *  ### Result Data Explain ###
     *  BUKRS : 회사코드
     *  PERNR : 사원번호
     *  tenantId : 테넌트 ID
     *  tenantKey : 테넌트 KEY (abx)
     *  data : 데이터
     * **/
    @POST("ZFI_ED_014_1")
    Call<JsonObject> getZFI_ED_014_1(@Body HashMap<String,Object> param);


    /***
     * @author hmyuk
     * @since 2019.6.18
     * @param {
     * 			"tenantId" : "",
     * 			"fileId" : "",
     * 		  }
     * @explain 해당 file ID 에 맞는이미지를 가져온다.
     * @return
     *  result : {
     *  			"resultYn" : "Y",
     *  			"message" : ""
     *  		 }
     *  ### Result Data Explain ###
     *  tenantId : 테넌트 ID
     *  tenantKey : 테넌트 KEY (abx)
     *  FILE_ID : 파일아이디
     *  data : 데이터
     * **/
    @POST("getImage")
    Call<JsonObject> getImage(@Body HashMap<String,Object> param);

}
