package com.example.campaign.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "必須項目です")
    @Pattern(regexp = "^[\\p{L}\\s]+$", message = "氏名は文字で入力してください")
    private String name;

    @NotBlank(message = "必須項目です")
    @Email(message = "メール形式が正しくありません")
    private String email;

    @NotBlank(message = "必須項目です")
    @Pattern(regexp = "^[0-9\\-]+$", message = "電話番号は数字またはハイフンで入力してください")
    private String phone;

    @NotBlank(message = "必須項目です")
    private String area;

    @Min(value = 0, message = "0以上で入力してください")
    private int age;
}