package msa.productservice.adapter.out.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductSearchESRepository
        extends ElasticsearchRepository<ProductSearchDoc, Long> {
}
