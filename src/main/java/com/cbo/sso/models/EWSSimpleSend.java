package com.cbo.sso.models;

import lombok.Data;


@Data
public class EWSSimpleSend {
    String[] Email;

    String Subject;

    String body;

    Boolean shortCircuit;
}
