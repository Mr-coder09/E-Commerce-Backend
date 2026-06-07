package com.example.Main.DTO;

import lombok.Data;

@Data
public class PaginationMeta {

	  private int page;
	    private int size;
	    private long totalElements;
	    private int totalPages;
}
