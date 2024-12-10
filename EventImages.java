package com.janaushadhi.adminservice.requestpayload;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class EventImages {
    private String imageUrl;
    private Short imageStatus;
}
