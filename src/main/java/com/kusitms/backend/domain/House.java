package com.kusitms.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kusitms.backend.dto.HouseDto;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class House extends MetaEntity {

  @Id
  @GeneratedValue
  @Column(name = "house_id")
  private Long id;

  @Embedded
  private Address address; // 도로명 주소

  private String price; // 구체적 가격
  private String size; // 집 크기
  private LocalDate createdDate; // 준공연도
  private String purpose; // 용도
  private String deposit; // 보증금
  private String maintenance; // 관리비
  private String monthlyRent; // 월세

  @JsonIgnore
  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "house_id")
  private Set<ImageFile> imageFileSet;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "owner_id")
  private User owner;

  public static House createHouse(HouseDto request, User user) {
    return House.builder()
        .address(Address.builder()
            .city(request.getCity())
            .street(request.getStreet())
            .zipcode(request.getZipcode())
            .build())
        .createdDate(request.getCreatedDate())
        .size(request.getSize())
        .owner(user)
        .price(request.getPrice())
        .purpose(request.getPurpose())
        .deposit(request.getDeposit())
        .maintenance(request.getMaintenance())
        .monthlyRent(request.getMonthlyRent())
        .imageFileSet(request.getImageUrls()
            .stream()
            .map(ImageFile::toEntity)
            .collect(Collectors.toSet()))
        .build();
  }
}
