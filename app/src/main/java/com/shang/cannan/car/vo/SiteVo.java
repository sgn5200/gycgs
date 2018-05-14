package com.shang.cannan.car.vo;

/**
 * Description    :
 * CreateAuthor: Cannan
 * CreateTime   : 2018/5/9     9:18
 * Project          : Car
 * PackageName :  com.shang.cannan.car.vo;
 */

public class SiteVo {
	/**
	 * BespeakSiteNo : 01
	 * BespeakSiteName : 贵阳西南国际商贸城车管大厅
	 * BespeakSiteRemark : 观山湖区贵阳西南国际商贸城二号广场B区G1、G2层
	 * Status : 1
	 * OrderNumber : 1
	 * BespeakService :
	 * BespeakItemGroupNo :
	 */

	private String BespeakSiteNo;
	private String BespeakSiteName;
	private String BespeakSiteRemark;
	private String Status;
	private int OrderNumber;
	private String BespeakService;
	private String BespeakItemGroupNo;

	public String getBespeakSiteNo() {
		return BespeakSiteNo;
	}

	public void setBespeakSiteNo(String BespeakSiteNo) {
		this.BespeakSiteNo = BespeakSiteNo;
	}

	public String getBespeakSiteName() {
		return BespeakSiteName;
	}

	public void setBespeakSiteName(String BespeakSiteName) {
		this.BespeakSiteName = BespeakSiteName;
	}

	public String getBespeakSiteRemark() {
		return BespeakSiteRemark;
	}

	public void setBespeakSiteRemark(String BespeakSiteRemark) {
		this.BespeakSiteRemark = BespeakSiteRemark;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String Status) {
		this.Status = Status;
	}

	public int getOrderNumber() {
		return OrderNumber;
	}

	public void setOrderNumber(int OrderNumber) {
		this.OrderNumber = OrderNumber;
	}

	public String getBespeakService() {
		return BespeakService;
	}

	public void setBespeakService(String BespeakService) {
		this.BespeakService = BespeakService;
	}

	public String getBespeakItemGroupNo() {
		return BespeakItemGroupNo;
	}

	public void setBespeakItemGroupNo(String BespeakItemGroupNo) {
		this.BespeakItemGroupNo = BespeakItemGroupNo;
	}
}
