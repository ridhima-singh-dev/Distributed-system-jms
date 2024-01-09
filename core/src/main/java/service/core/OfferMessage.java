package service.core;

import java.io.Serializable;
import java.util.LinkedList;

public class OfferMessage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1324744418118206945L;
	private ClientInfo info;
	private LinkedList<Quotation> quotations;

	public OfferMessage(ClientInfo info, LinkedList<Quotation> quotations) {
		this.info = info;
		this.quotations = quotations;
	}

	public ClientInfo getInfo() {
		return info;
	}

	public LinkedList<Quotation> getQuotations() {
		return quotations;
	}
}
