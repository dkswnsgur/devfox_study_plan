# junit Bank App 

@EnableJpaAuditing (Main 클래스)
@EntityListeners(AuditingEntityListener.class) (Entity 클래스)

@CreatedDate // Insert
@Column(nullable = false)
private LocalDateTime createdAt;

    @LastModifiedDate // Insert, Update
    @Column(nullable = false)
    private LocalDateTime updatedAt;