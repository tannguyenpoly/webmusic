package com.fpoly.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Getter
@Setter
@Table(name = "token_transactions")
public class TokenTransaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_id")
	private Long userId;

	@ManyToOne
	@JoinColumn(name = "package_id")
	private TokenPackage tokenPackage;

	@Column(name = "amount_paid", precision = 10, scale = 2)
	private BigDecimal amountPaid;

	@Column(name = "token_amount")
	private Integer tokenAmount;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", length = 10)
	private TokenTransactionStatus status;

	@Column(name = "vnpay_txn_ref", length = 100)
	private String vnpayTxnRef;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@PrePersist
	public void prePersist() {
		if (createdAt == null) {
			createdAt = LocalDateTime.now();
		}
	}
}