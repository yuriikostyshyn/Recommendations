package com.flux.recommendations.model;

import java.math.BigDecimal;

public class MeanRating implements Comparable<MeanRating> {
	private String itemName;
	private BigDecimal meanData;

	public MeanRating(String itemName, BigDecimal meanData) {
		this.itemName = itemName;
		this.meanData = meanData;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemNames) {
		this.itemName = itemNames;
	}

	public BigDecimal getMeanData() {
		return meanData;
	}

	public void setMeanData(BigDecimal meanData) {
		this.meanData = meanData;
	}

	public boolean equals(Object o) {
		if (o == null) {
			return false;
		} else if (!(o instanceof MeanRating)) {
			return false;
		} else if (o == this) {
			return true;
		} else {
			MeanRating toCompare = (MeanRating) o;
			if (toCompare.getItemName().equals(this.getItemName()) && toCompare.getMeanData().equals(this.meanData)) {
				return true;
			}
		}
		return false;
	}

	public String toString() {
		return "item name:" + itemName + ", value:" + meanData;
	}

    @Override
    public int compareTo(MeanRating o) {
        return - this.meanData.compareTo(o.getMeanData());
    }
}
