package msa.productservice.application.port.service;

import lombok.RequiredArgsConstructor;
import msa.productservice.adapter.in.web.dto.ProductRequest;
import msa.productservice.adapter.in.web.dto.ProductResponse;
import msa.productservice.application.port.in.ProductCommandUseCase;
import msa.productservice.application.port.out.ProductPersistencePort;
import msa.productservice.config.MDCHelper;
import msa.productservice.domain.ProductMaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductCommandService implements ProductCommandUseCase {
    private final ProductPersistencePort productPersistencePort;
    private static final Logger log = LoggerFactory.getLogger(ProductCommandService.class);

    @Override
    public ProductResponse registerProduct(ProductRequest req, String userId, String roleName) {
        log.info("[상품등록][시작] 상품명: {}, 등록자: {}", req.getProductName(), userId);
        MDCHelper.appendDebug(this.getClass(), "상품 등록 요청. 상품명: " + req.getProductName() + ", 등록자: " + userId);

        try {
            if (productPersistencePort.findByProductName(req.getProductName()).isPresent()) {
                log.warn("[상품등록][중복][실패] 상품명: {}, 등록자: {}", req.getProductName(), userId);
                MDCHelper.appendDebug(this.getClass(), "상품명 중복. 등록 실패: " + req.getProductName());
                return ProductResponse.fail(false, "이미 존재하는 상품명입니다.");
            }
            String productRegisterType;
            switch (roleName) {
                case "ADMIN":
                    productRegisterType = "A";
                    break;
                case "USER":
                    productRegisterType = "U";
                    break;
                default:
                    productRegisterType = "G";
            }

            ProductMaster productMaster = ProductMaster.builder()
                    .clientCode(req.getClientCode())
                    .productType(req.getProductType())
                    .productRole(req.getProductRole())
                    .supplierCode(req.getSupplierCode())
                    .productName(req.getProductName())
                    .productGroup(req.getProductGroup())
                    .productYear(req.getProductYear())
                    .productSeason(req.getProductSeason())
                    .brand(req.getBrand())
                    .style(req.getStyle())
                    .color(req.getColor())
                    .size(req.getSize())
                    .productSku(req.getProductSku())
                    .productOrigin(req.getProductOrigin())
                    .productUnit(req.getProductUnit())
                    .storageTemperature(req.getStorageTemperature())
                    .retailPrice(req.getRetailPrice())
                    .deliveryType(req.getDeliveryType())
                    .remarks(req.getRemarks())
                    .expireDateUseYn(req.getExpireDateUseYn())
                    .salePeriodType(req.getSalePeriodType())
                    .useYn(req.getUseYn())
                    .productRegisterType(productRegisterType)
                    .registerId(userId)
                    .purchasePrice(req.getPurchasePrice())
                    .build();

            ProductMaster saved = productPersistencePort.save(productMaster);

            log.info("[상품등록][성공] 상품ID: {}, 상품명: {}, 등록자: {}", saved.getProductCode(), saved.getProductName(), userId);
            MDCHelper.appendDebug(this.getClass(), "상품 등록 성공. 상품ID: " + saved.getProductCode());

            return ProductResponse.success(true, "상품 등록 성공");

        } catch (Exception e) {
            log.error("[상품등록][예외][실패] 상품명: {}, 등록자: {} - {}", req.getProductName(), userId, e.getMessage(), e);
            MDCHelper.appendDebug(this.getClass(), "상품 등록 예외 발생: " + e.getMessage());
            return ProductResponse.fail(false, "상품 등록 중 서버 오류가 발생했습니다.");
        }
    }
}
