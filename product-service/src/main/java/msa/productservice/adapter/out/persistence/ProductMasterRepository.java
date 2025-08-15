package msa.productservice.adapter.out.persistence;

import feign.Param;
import msa.productservice.adapter.in.web.dto.ProductWithClientDto;
import msa.productservice.domain.ProductMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductMasterRepository extends JpaRepository<ProductMaster, Long> {

    // 기존: 상품명 중복 체크
    Optional<ProductMaster> findByProductName(String productName);

    //  ES 부분 업데이트용: 화주(clientCode)에 속한 상품들의 productCode 목록
    @Query("select p.productCode from ProductMaster p where p.clientCode = :clientCode")
    List<Long> findProductCodesByClientCode(Long clientCode);

    // (옵션) 대량일 때 안전하게 페이지로 잘라서 조회하고 싶을 때 사용
    @Query(
            value = "select p.productCode from ProductMaster p where p.clientCode = :clientCode order by p.productCode",
            countQuery = "select count(p) from ProductMaster p where p.clientCode = :clientCode"
    )
    Page<Long> findProductCodesPageByClientCode(Long clientCode, Pageable pageable);

    // (옵션) 총 개수만 빠르게 알고 싶을 때
    long countByClientCode(Long clientCode);

    // ★ 조인 + 페이징
    @Query(
            value =
                    "select new msa.productservice.adapter.in.web.dto.ProductWithClientDto(" +
                            "  p.productCode, p.productName, p.brand, p.retailPrice, " +
                            "  c.clientCode, c.clientName, c.businessCode" +
                            ") " +
                            "from ProductMaster p " +
                            "join ClientMaster c on p.clientCode = c.clientCode " +
                            "where (:clientCode is null or p.clientCode = :clientCode) " +
                            "  and (:keyword is null or " +
                            "       lower(p.productName) like lower(concat('%', :keyword, '%')) or " +
                            "       lower(p.brand)       like lower(concat('%', :keyword, '%')) or " +
                            "       lower(p.style)       like lower(concat('%', :keyword, '%')) or " +
                            "       lower(p.color)       like lower(concat('%', :keyword, '%')) or " +
                            "       lower(p.size)        like lower(concat('%', :keyword, '%')) or " +
                            "       lower(p.productSku)  like lower(concat('%', :keyword, '%')) or " +
                            "       lower(c.clientName)  like lower(concat('%', :keyword, '%')) " +
                            "  )",
            countQuery =
                    "select count(p) " +
                            "from ProductMaster p " +
                            "join ClientMaster c on p.clientCode = c.clientCode " +
                            "where (:clientCode is null or p.clientCode = :clientCode) " +
                            "  and (:keyword is null or " +
                            "       lower(p.productName) like lower(concat('%', :keyword, '%')) or " +
                            "       lower(p.brand)       like lower(concat('%', :keyword, '%')) or " +
                            "       lower(p.style)       like lower(concat('%', :keyword, '%')) or " +
                            "       lower(p.color)       like lower(concat('%', :keyword, '%')) or " +
                            "       lower(p.size)        like lower(concat('%', :keyword, '%')) or " +
                            "       lower(p.productSku)  like lower(concat('%', :keyword, '%')) or " +
                            "       lower(c.clientName)  like lower(concat('%', :keyword, '%')) " +
                            "  )"
    )
    Page<ProductWithClientDto> searchProductsWithClients(
            @Param("clientCode") Long clientCode,
            @Param("keyword") String keyword,
            Pageable pageable
    );
}
