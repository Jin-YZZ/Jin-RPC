package com.jin.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class AccountDto implements Serializable {
    private double comsumer ;
    private String userId;
    private  int type;
}
