package com.activity.squad2.service;

import org.springframework.http.ResponseEntity;

public interface ICMAPService {
    ResponseEntity<?> getICMAPData(String firstName, String lastName);
}
