package com.main.traveltour.dto.customer.infomation;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ChangePassDto implements Serializable {

    String newPass;

    String confirmPass;
}
