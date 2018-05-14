package com.shang.cannan.car.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Description    :
 * CreateAuthor: Cannan
 * CreateTime   : 2018/5/9     14:31
 * Project          : Car
 * PackageName :  com.shang.cannan.car.vo;
 */

public class OwnerVo implements Parcelable {
	private String  __RequestVerificationToken;  //令牌
	private int CarServiceNo;                             //业务类型
	private int NumberType;                              //车辆类型
	private String IdentCode;                            //识别代号
	private String OwnerName;                         //名字
	private int OwnerType;                              //1个人 2 单位
	private int CardType;                          // 证件号类型 身份证 1   机构2  其他3
	private String CardCode;                          //证件号

	private int updateStatus;    //填报过1
	private int okStatus;    //预约成功1
	private String createTime; //创建时间
	private boolean checked;

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public int getOkStatus() {
		return okStatus;
	}

	public int getUpdateStatus() {
		return updateStatus;
	}

	public void setOkStatus(int okStatus) {
		this.okStatus = okStatus;
	}

	public void setUpdateStatus(int updateStatus) {
		this.updateStatus = updateStatus;
	}

	public String get__RequestVerificationToken() {
		return __RequestVerificationToken;
	}

	public void set__RequestVerificationToken(String __RequestVerificationToken) {
		this.__RequestVerificationToken = __RequestVerificationToken;
	}

	public int getCarServiceNo() {
		return CarServiceNo;
	}

	public void setCarServiceNo(int carServiceNo) {
		CarServiceNo = carServiceNo;
	}

	public int getNumberType() {
		return NumberType;
	}

	public void setNumberType(int numberType) {
		NumberType = numberType;
	}

	public String getIdentCode() {
		return IdentCode;
	}

	public void setIdentCode(String identCode) {
		IdentCode = identCode;
	}

	public String getOwnerName() {
		return OwnerName;
	}

	public void setOwnerName(String ownerName) {
		OwnerName = ownerName;
	}

	public int getOwnerType() {
		return OwnerType;
	}

	public void setOwnerType(int ownerType) {
		OwnerType = ownerType;
	}

	public int getCardType() {
		return CardType;
	}

	public void setCardType(int cardType) {
		CardType = cardType;
	}

	public String getCardCode() {
		return CardCode;
	}

	public void setCardCode(String cardCode) {
		CardCode = cardCode;
	}

	@Override
	public String toString() {
		return "OwnerVo{" +
				"__RequestVerificationToken='" + __RequestVerificationToken + '\'' +
				", CarServiceNo=" + CarServiceNo +
				", NumberType=" + NumberType +
				", IdentCode='" + IdentCode + '\'' +
				", OwnerName='" + OwnerName + '\'' +
				", OwnerType=" + OwnerType +
				", CardType='" + CardType + '\'' +
				", CardCode='" + CardCode + '\'' +
				'}';
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.__RequestVerificationToken);
		dest.writeInt(this.CarServiceNo);
		dest.writeInt(this.NumberType);
		dest.writeString(this.IdentCode);
		dest.writeString(this.OwnerName);
		dest.writeInt(this.OwnerType);
		dest.writeInt(this.CardType);
		dest.writeString(this.CardCode);
		dest.writeInt(this.updateStatus);
		dest.writeInt(this.okStatus);
		dest.writeString(this.createTime);
	}

	public OwnerVo() {
	}

	protected OwnerVo(Parcel in) {
		this.__RequestVerificationToken = in.readString();
		this.CarServiceNo = in.readInt();
		this.NumberType = in.readInt();
		this.IdentCode = in.readString();
		this.OwnerName = in.readString();
		this.OwnerType = in.readInt();
		this.CardType = in.readInt();
		this.CardCode = in.readString();
		this.updateStatus = in.readInt();
		this.okStatus = in.readInt();
		this.createTime = in.readString();
	}

	public static final Parcelable.Creator<OwnerVo> CREATOR = new Parcelable.Creator<OwnerVo>() {
		@Override
		public OwnerVo createFromParcel(Parcel source) {
			return new OwnerVo(source);
		}

		@Override
		public OwnerVo[] newArray(int size) {
			return new OwnerVo[size];
		}
	};

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		OwnerVo ownerVo = (OwnerVo) o;

		if (updateStatus != ownerVo.updateStatus) return false;
		if (okStatus != ownerVo.okStatus) return false;
		if (!CardCode.equals(ownerVo.CardCode)) return false;
		return createTime.equals(ownerVo.createTime);
	}

	@Override
	public int hashCode() {
		int result = CardCode.hashCode();
		result = 31 * result + updateStatus;
		result = 31 * result + okStatus;
		result = 31 * result + createTime.hashCode();
		return result;
	}
}
