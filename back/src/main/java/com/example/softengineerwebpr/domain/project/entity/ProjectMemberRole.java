package com.example.softengineerwebpr.domain.project.entity;

public enum ProjectMemberRole {
    관리자, // ADMIN
    일반사용자 // USER (DB 스키마 ENUM 값과 일치 필요: '관리자', '일반 사용자')
}