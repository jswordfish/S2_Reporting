package com.innowave.mahaulb.reports.data;

import java.io.File;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;
@Entity
@NamedQueries({
	@NamedQuery(name="ULBLogoMapper.getUniqueULBLogoMapper", 
			query="SELECT r FROM ULBLogoMapper r WHERE r.ulbName=:ulbName")
})
public class ULBLogoMapper extends Base{
	
	String ulbName;
	
	String ulbId;
	
	String leftLogoPath;
	
	@Transient
	byte[] leftLogo;
	
	@Transient
	String leftLogoFileExtension;
	
	String rightLogoPath;
	
	@Transient
	byte[] rightLogo;
	
	@Transient
	String rightLogoFileExtension;
	
	@Transient
	String letLogoFileName;
	
	@Transient
	String rightLogoFileName;

	public String getUlbName() {
		return ulbName;
	}

	public void setUlbName(String ulbName) {
		this.ulbName = ulbName;
	}

	public String getUlbId() {
		return ulbId;
	}

	public void setUlbId(String ulbId) {
		this.ulbId = ulbId;
	}

	public String getLeftLogoPath() {
		return leftLogoPath;
	}

	public void setLeftLogoPath(String leftLogoPath) {
		this.leftLogoPath = leftLogoPath;
		if(leftLogoPath != null) {
			letLogoFileName = this.leftLogoPath.substring(this.leftLogoPath.lastIndexOf(File.separator)+1, this.leftLogoPath.length());
		}
	}

	public byte[] getLeftLogo() {
		return leftLogo;
	}

	public void setLeftLogo(byte[] leftLogo) {
		this.leftLogo = leftLogo;
	}

	public String getLeftLogoFileExtension() {
		return leftLogoFileExtension;
	}

	public void setLeftLogoFileExtension(String leftLogoFileExtension) {
		this.leftLogoFileExtension = leftLogoFileExtension;
	}

	public String getRightLogoPath() {
		return rightLogoPath;
	}

	public void setRightLogoPath(String rightLogoPath) {
		this.rightLogoPath = rightLogoPath;
		if(rightLogoPath != null) {
			rightLogoFileName = this.rightLogoPath.substring(this.rightLogoPath.lastIndexOf(File.separator)+1, this.rightLogoPath.length());
		}
	}

	public byte[] getRightLogo() {
		return rightLogo;
	}

	public void setRightLogo(byte[] rightLogo) {
		this.rightLogo = rightLogo;
	}

	public String getRightLogoFileExtension() {
		return rightLogoFileExtension;
	}

	public void setRightLogoFileExtension(String rightLogoFileExtension) {
		this.rightLogoFileExtension = rightLogoFileExtension;
	}

	public String getLetLogoFileName() {
		if(leftLogoPath != null) {
			letLogoFileName = this.leftLogoPath.substring(this.leftLogoPath.lastIndexOf(File.separator)+1, this.leftLogoPath.length());
		}
		return letLogoFileName;
	}

	public void setLetLogoFileName(String letLogoFileName) {
		this.letLogoFileName = letLogoFileName;
	}

	public String getRightLogoFileName() {
		if(rightLogoPath != null) {
			rightLogoFileName = this.rightLogoPath.substring(this.rightLogoPath.lastIndexOf(File.separator)+1, this.rightLogoPath.length());
		}
		return rightLogoFileName;
	}

	public void setRightLogoFileName(String rightLogoFileName) {
		this.rightLogoFileName = rightLogoFileName;
	}

	
	
	
}
