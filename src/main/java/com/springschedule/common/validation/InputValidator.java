package com.springschedule.common.validation;

public class InputValidator {

    // 텍스트 공백 검증
    public static void requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + "비었음. 작성해 주세요.");
        }
    }

    // 최대 길이 제한 검증
    public static void requireMaxLength(String value, String fieldName, int maxLength) {
        if (value.length() > maxLength) {
            throw new IllegalArgumentException(fieldName + " 이건 최대 " + maxLength + "자까지 가능합니다......");
        }
    }
}
