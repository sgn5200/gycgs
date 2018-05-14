package com.shang.cannan.car.vo;

import java.util.List;

/**
 * Description    :
 * CreateAuthor: Cannan
 * CreateTime   : 2018/5/9     10:37
 * Project          : Car
 * PackageName :  com.shang.cannan.car.vo;
 */

public class MayInfoVo {
	/**
	 * SerialNumber : 4
	 * BespeakDate : /Date(1526054400000)/
	 * BespeakNumbers : []
	 * BespeakNumberString : 不可预约
	 * Remark : 周六不可预约
	 * WeekDay : 6
	 * AmMayBespeakNumber : 不可预约
	 * PmMayBespeakNumber : 不可预约
	 * OtherMayBespeakNumber : 不可预约
	 * BakAmMayBespeakNumber : 不可预约
	 * BakPmMayBespeakNumber : 不可预约
	 * BakOtherMayBespeakNumber : 不可预约
	 * BespeakDateHtml : 2018-05-12
	 * RemarkHtml : 周六不可预约
	 * BespeakSiteNo : 05
	 * BespeakSiteName : 合朋机动车登记服务站
	 * BespeakServiceNo : 1
	 * BespeakServiceName : 注册登记（非专段号牌、新能源）
	 * BespeakItemNo : 1
	 * BespeakItemName : 国产小型客车
	 * IsMayBespeak : 0
	 * BespeakConfigId : 187
	 * ReleaseListId : 188984
	 * BespeakServices : [{"BespeakServiceNo":"1","BespeakServiceName":"注册登记（非专段号牌、新能源）","BespeakServiceRemark":"","Status":"","OrderNumber":"","BespeakServiceType":"","BespeakItemGroupNo":"","BespeakItem":"","BespeakItemNo":"","BespeakItemName":""},{"BespeakServiceNo":"12","BespeakServiceName":"加装/拆除操纵辅助装置","BespeakServiceRemark":"","Status":"","OrderNumber":"","BespeakServiceType":"","BespeakItemGroupNo":"","BespeakItem":"","BespeakItemNo":"","BespeakItemName":""},{"BespeakServiceNo":"13","BespeakServiceName":"补领登记证书","BespeakServiceRemark":"","Status":"","OrderNumber":"","BespeakServiceType":"","BespeakItemGroupNo":"","BespeakItem":"","BespeakItemNo":"","BespeakItemName":""},{"BespeakServiceNo":"14","BespeakServiceName":"更换整车（非专段号牌）","BespeakServiceRemark":"","Status":"","OrderNumber":"","BespeakServiceType":"","BespeakItemGroupNo":"","BespeakItem":"","BespeakItemNo":"","BespeakItemName":""},{"BespeakServiceNo":"2","BespeakServiceName":"转入（非专段号牌）","BespeakServiceRemark":"","Status":"","OrderNumber":"","BespeakServiceType":"","BespeakItemGroupNo":"","BespeakItem":"","BespeakItemNo":"","BespeakItemName":""},{"BespeakServiceNo":"4","BespeakServiceName":"转移登记（过户到非专段号牌）","BespeakServiceRemark":"","Status":"","OrderNumber":"","BespeakServiceType":"","BespeakItemGroupNo":"","BespeakItem":"","BespeakItemNo":"","BespeakItemName":""},{"BespeakServiceNo":"6","BespeakServiceName":"变更车身颜色","BespeakServiceRemark":"","Status":"","OrderNumber":"","BespeakServiceType":"","BespeakItemGroupNo":"","BespeakItem":"","BespeakItemNo":"","BespeakItemName":""},{"BespeakServiceNo":"7","BespeakServiceName":"更换车身或者车架","BespeakServiceRemark":"","Status":"","OrderNumber":"","BespeakServiceType":"","BespeakItemGroupNo":"","BespeakItem":"","BespeakItemNo":"","BespeakItemName":""},{"BespeakServiceNo":"8","BespeakServiceName":"更换发动机","BespeakServiceRemark":"","Status":"","OrderNumber":"","BespeakServiceType":"","BespeakItemGroupNo":"","BespeakItem":"","BespeakItemNo":"","BespeakItemName":""},{"BespeakServiceNo":"9","BespeakServiceName":"变更使用性质","BespeakServiceRemark":"","Status":"","OrderNumber":"","BespeakServiceType":"","BespeakItemGroupNo":"","BespeakItem":"","BespeakItemNo":"","BespeakItemName":""}]
	 * BespeakItems : [{"BespeakItemNo":"1","BespeakItemGroupNo":"","BespeakItemName":"国产小型客车","BespeakItemRemark":"","Status":"","OrderNumber":""},{"BespeakItemNo":"17","BespeakItemGroupNo":"","BespeakItemName":"国产小型出租客运客车","BespeakItemRemark":"","Status":"","OrderNumber":""},{"BespeakItemNo":"18","BespeakItemGroupNo":"","BespeakItemName":"进口小型出租客运客车","BespeakItemRemark":"","Status":"","OrderNumber":""},{"BespeakItemNo":"19","BespeakItemGroupNo":"","BespeakItemName":"国产小型旅游客运客车","BespeakItemRemark":"","Status":"","OrderNumber":""},{"BespeakItemNo":"2","BespeakItemGroupNo":"","BespeakItemName":"进口小型客车","BespeakItemRemark":"","Status":"","OrderNumber":""},{"BespeakItemNo":"20","BespeakItemGroupNo":"","BespeakItemName":"进口小型旅游客运客车","BespeakItemRemark":"","Status":"","OrderNumber":""},{"BespeakItemNo":"3","BespeakItemGroupNo":"","BespeakItemName":"国产小型货车","BespeakItemRemark":"","Status":"","OrderNumber":""},{"BespeakItemNo":"4","BespeakItemGroupNo":"","BespeakItemName":"进口小型货车","BespeakItemRemark":"","Status":"","OrderNumber":""},{"BespeakItemNo":"8","BespeakItemGroupNo":"","BespeakItemName":"低速车","BespeakItemRemark":"","Status":"","OrderNumber":""}]
	 */

	private int SerialNumber;
	private String BespeakDate;
	private String BespeakNumberString;
	private String Remark;
	private String WeekDay;
	private String AmMayBespeakNumber;
	private String PmMayBespeakNumber;
	private String OtherMayBespeakNumber;
	private String BakAmMayBespeakNumber;
	private String BakPmMayBespeakNumber;
	private String BakOtherMayBespeakNumber;
	private String BespeakDateHtml;
	private String RemarkHtml;
	private String BespeakSiteNo;
	private String BespeakSiteName;
	private String BespeakServiceNo;
	private String BespeakServiceName;
	private String BespeakItemNo;
	private String BespeakItemName;
	private String IsMayBespeak;
	private int BespeakConfigId;
	private int ReleaseListId;
	private List<?> BespeakNumbers;
	private List<BespeakServicesBean> BespeakServices;
	private List<BespeakItemsBean> BespeakItems;

	public int getSerialNumber() {
		return SerialNumber;
	}

	public void setSerialNumber(int SerialNumber) {
		this.SerialNumber = SerialNumber;
	}

	public String getBespeakDate() {
		return BespeakDate;
	}

	public void setBespeakDate(String BespeakDate) {
		this.BespeakDate = BespeakDate;
	}

	public String getBespeakNumberString() {
		return BespeakNumberString;
	}

	public void setBespeakNumberString(String BespeakNumberString) {
		this.BespeakNumberString = BespeakNumberString;
	}

	public String getRemark() {
		return Remark;
	}

	public void setRemark(String Remark) {
		this.Remark = Remark;
	}

	public String getWeekDay() {
		return WeekDay;
	}

	public void setWeekDay(String WeekDay) {
		this.WeekDay = WeekDay;
	}

	public String getAmMayBespeakNumber() {
		return AmMayBespeakNumber;
	}

	public void setAmMayBespeakNumber(String AmMayBespeakNumber) {
		this.AmMayBespeakNumber = AmMayBespeakNumber;
	}

	public String getPmMayBespeakNumber() {
		return PmMayBespeakNumber;
	}

	public void setPmMayBespeakNumber(String PmMayBespeakNumber) {
		this.PmMayBespeakNumber = PmMayBespeakNumber;
	}

	public String getOtherMayBespeakNumber() {
		return OtherMayBespeakNumber;
	}

	public void setOtherMayBespeakNumber(String OtherMayBespeakNumber) {
		this.OtherMayBespeakNumber = OtherMayBespeakNumber;
	}

	public String getBakAmMayBespeakNumber() {
		return BakAmMayBespeakNumber;
	}

	public void setBakAmMayBespeakNumber(String BakAmMayBespeakNumber) {
		this.BakAmMayBespeakNumber = BakAmMayBespeakNumber;
	}

	public String getBakPmMayBespeakNumber() {
		return BakPmMayBespeakNumber;
	}

	public void setBakPmMayBespeakNumber(String BakPmMayBespeakNumber) {
		this.BakPmMayBespeakNumber = BakPmMayBespeakNumber;
	}

	public String getBakOtherMayBespeakNumber() {
		return BakOtherMayBespeakNumber;
	}

	public void setBakOtherMayBespeakNumber(String BakOtherMayBespeakNumber) {
		this.BakOtherMayBespeakNumber = BakOtherMayBespeakNumber;
	}

	public String getBespeakDateHtml() {
		return BespeakDateHtml;
	}

	public void setBespeakDateHtml(String BespeakDateHtml) {
		this.BespeakDateHtml = BespeakDateHtml;
	}

	public String getRemarkHtml() {
		return RemarkHtml;
	}

	public void setRemarkHtml(String RemarkHtml) {
		this.RemarkHtml = RemarkHtml;
	}

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

	public String getBespeakServiceNo() {
		return BespeakServiceNo;
	}

	public void setBespeakServiceNo(String BespeakServiceNo) {
		this.BespeakServiceNo = BespeakServiceNo;
	}

	public String getBespeakServiceName() {
		return BespeakServiceName;
	}

	public void setBespeakServiceName(String BespeakServiceName) {
		this.BespeakServiceName = BespeakServiceName;
	}

	public String getBespeakItemNo() {
		return BespeakItemNo;
	}

	public void setBespeakItemNo(String BespeakItemNo) {
		this.BespeakItemNo = BespeakItemNo;
	}

	public String getBespeakItemName() {
		return BespeakItemName;
	}

	public void setBespeakItemName(String BespeakItemName) {
		this.BespeakItemName = BespeakItemName;
	}

	public String getIsMayBespeak() {
		return IsMayBespeak;
	}

	public void setIsMayBespeak(String IsMayBespeak) {
		this.IsMayBespeak = IsMayBespeak;
	}

	public int getBespeakConfigId() {
		return BespeakConfigId;
	}

	public void setBespeakConfigId(int BespeakConfigId) {
		this.BespeakConfigId = BespeakConfigId;
	}

	public int getReleaseListId() {
		return ReleaseListId;
	}

	public void setReleaseListId(int ReleaseListId) {
		this.ReleaseListId = ReleaseListId;
	}

	public List<?> getBespeakNumbers() {
		return BespeakNumbers;
	}

	public void setBespeakNumbers(List<?> BespeakNumbers) {
		this.BespeakNumbers = BespeakNumbers;
	}

	public List<BespeakServicesBean> getBespeakServices() {
		return BespeakServices;
	}

	public void setBespeakServices(List<BespeakServicesBean> BespeakServices) {
		this.BespeakServices = BespeakServices;
	}

	public List<BespeakItemsBean> getBespeakItems() {
		return BespeakItems;
	}

	public void setBespeakItems(List<BespeakItemsBean> BespeakItems) {
		this.BespeakItems = BespeakItems;
	}

	public static class BespeakServicesBean {
		/**
		 * BespeakServiceNo : 1
		 * BespeakServiceName : 注册登记（非专段号牌、新能源）
		 * BespeakServiceRemark :
		 * Status :
		 * OrderNumber :
		 * BespeakServiceType :
		 * BespeakItemGroupNo :
		 * BespeakItem :
		 * BespeakItemNo :
		 * BespeakItemName :
		 */

		private String BespeakServiceNo;
		private String BespeakServiceName;
		private String BespeakServiceRemark;
		private String Status;
		private String OrderNumber;
		private String BespeakServiceType;
		private String BespeakItemGroupNo;
		private String BespeakItem;
		private String BespeakItemNo;
		private String BespeakItemName;

		public String getBespeakServiceNo() {
			return BespeakServiceNo;
		}

		public void setBespeakServiceNo(String BespeakServiceNo) {
			this.BespeakServiceNo = BespeakServiceNo;
		}

		public String getBespeakServiceName() {
			return BespeakServiceName;
		}

		public void setBespeakServiceName(String BespeakServiceName) {
			this.BespeakServiceName = BespeakServiceName;
		}

		public String getBespeakServiceRemark() {
			return BespeakServiceRemark;
		}

		public void setBespeakServiceRemark(String BespeakServiceRemark) {
			this.BespeakServiceRemark = BespeakServiceRemark;
		}

		public String getStatus() {
			return Status;
		}

		public void setStatus(String Status) {
			this.Status = Status;
		}

		public String getOrderNumber() {
			return OrderNumber;
		}

		public void setOrderNumber(String OrderNumber) {
			this.OrderNumber = OrderNumber;
		}

		public String getBespeakServiceType() {
			return BespeakServiceType;
		}

		public void setBespeakServiceType(String BespeakServiceType) {
			this.BespeakServiceType = BespeakServiceType;
		}

		public String getBespeakItemGroupNo() {
			return BespeakItemGroupNo;
		}

		public void setBespeakItemGroupNo(String BespeakItemGroupNo) {
			this.BespeakItemGroupNo = BespeakItemGroupNo;
		}

		public String getBespeakItem() {
			return BespeakItem;
		}

		public void setBespeakItem(String BespeakItem) {
			this.BespeakItem = BespeakItem;
		}

		public String getBespeakItemNo() {
			return BespeakItemNo;
		}

		public void setBespeakItemNo(String BespeakItemNo) {
			this.BespeakItemNo = BespeakItemNo;
		}

		public String getBespeakItemName() {
			return BespeakItemName;
		}

		public void setBespeakItemName(String BespeakItemName) {
			this.BespeakItemName = BespeakItemName;
		}
	}

	public static class BespeakItemsBean {
		/**
		 * BespeakItemNo : 1
		 * BespeakItemGroupNo :
		 * BespeakItemName : 国产小型客车
		 * BespeakItemRemark :
		 * Status :
		 * OrderNumber :
		 */

		private String BespeakItemNo;
		private String BespeakItemGroupNo;
		private String BespeakItemName;
		private String BespeakItemRemark;
		private String Status;
		private String OrderNumber;

		public String getBespeakItemNo() {
			return BespeakItemNo;
		}

		public void setBespeakItemNo(String BespeakItemNo) {
			this.BespeakItemNo = BespeakItemNo;
		}

		public String getBespeakItemGroupNo() {
			return BespeakItemGroupNo;
		}

		public void setBespeakItemGroupNo(String BespeakItemGroupNo) {
			this.BespeakItemGroupNo = BespeakItemGroupNo;
		}

		public String getBespeakItemName() {
			return BespeakItemName;
		}

		public void setBespeakItemName(String BespeakItemName) {
			this.BespeakItemName = BespeakItemName;
		}

		public String getBespeakItemRemark() {
			return BespeakItemRemark;
		}

		public void setBespeakItemRemark(String BespeakItemRemark) {
			this.BespeakItemRemark = BespeakItemRemark;
		}

		public String getStatus() {
			return Status;
		}

		public void setStatus(String Status) {
			this.Status = Status;
		}

		public String getOrderNumber() {
			return OrderNumber;
		}

		public void setOrderNumber(String OrderNumber) {
			this.OrderNumber = OrderNumber;
		}
	}
}
