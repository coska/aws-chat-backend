package com.coska.aws.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {

	private String roomId;
	private String from;
	private String to;
	private String message;

	public Message(String from, String to, String message) {
		super();
		this.from = from;
		this.to = to;
		this.message = message;
	}

}