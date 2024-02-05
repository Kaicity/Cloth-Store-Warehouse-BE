package com.example.ctapi.serviceImpl;

import com.example.ctapi.dtos.Response.*;
import com.example.ctapi.Mappers.IColorsMapper;
import com.example.ctapi.Mappers.IOptionProductMapper;
import com.example.ctapi.Mappers.IProductMapper;
import com.example.ctapi.Mappers.ISizesMapper;
import com.example.ctapi.services.IProductService;
import com.example.ctcommondal.repository.IColorsRepository;
import com.example.ctcommondal.repository.IOptionProductRespository;
import com.example.ctcommondal.repository.IProductRepository;
import com.example.ctcommondal.repository.ISizesRepository;
import com.example.ctcommondal.entity.ColorsEntity;
import com.example.ctcommondal.entity.OptionProductEntity;
import com.example.ctcommondal.entity.ProductEntity;
import com.example.ctcommondal.entity.SizesEntity;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IProductServiceImpl implements IProductService {
    private static final Logger logger = LoggerFactory.getLogger(IProductServiceImpl.class);
    private final IProductRepository productRepository;
    private final IColorsRepository colorsRepository;
    private final ISizesRepository sizesRepository;
    private final IOptionProductRespository optionProductRespository;

    @Override
    public ProductSearchDto getAllProductUseBaseSearch() {
        try {
            int i = 0;
            // lay ds OptionProduct
            List<OptionProductEntity> optionProductEntities = optionProductRespository.findAll();
            List<OptionProductDto> optionProductDtos = IOptionProductMapper.INSTANCE.toListOptiondtoformEntity(optionProductEntities);

            List<ProductEntity> productEntityList = productRepository.findAll();
            List<ProductDto> products = IProductMapper.INSTANCE.toProductDtoListFromEntityList(productEntityList);
            List<String> productIDs = products.stream().map(ProductDto::getId).collect(Collectors.toList());

            List<ColorsEntity> colorsEntities = colorsRepository.findAllSizesByProductIds(productIDs);
            List<ColorsDto> colorDtos = IColorsMapper.INSTANCE.toColorListFromEntityList(colorsEntities);
            for (ColorsDto colorsDto : colorDtos) {
                List<OptionProductDto> result = optionProductDtos.stream()
                        .filter(option -> colorsDto.getOptionProduct().getId().equals(option.getId()))
                        .collect(Collectors.toList());

                colorsDto.setOptionProduct(result.size() == 0 ? null : result.get(0));
            }

            List<SizesEntity> sizesEntities = sizesRepository.findAllColorsbyProductIds(productIDs);
            List<SizesDto> sizesDtos = ISizesMapper.INSTANCE.toSizesDtoListFromEntityList(sizesEntities);
            for (SizesDto sizesDto : sizesDtos) {
                List<OptionProductDto> result = optionProductDtos.stream()
                        .filter(option -> sizesDto.getOptionProduct().getId().equals(option.getId()))
                        .collect(Collectors.toList());

                sizesDto.setOptionProduct(result.size() == 0 ? null : result.get(0));
            }

            for (ProductDto product : products) {
                List<ColorsDto> colorsList = colorDtos.stream()
                        .filter(obj -> obj.getProduct().getId().equals(product.getId()))
                        .collect(Collectors.toList());
                colorsList.forEach(color -> color.setProduct(null));
                colorDtos.removeAll(colorsList);

                List<SizesDto> sizesList = sizesDtos.stream()
                        .filter(obj -> obj.getProduct().getId().equals(product.getId()))
                        .collect(Collectors.toList());

                sizesList.forEach(size -> size.setProduct(null));
                sizesDtos.removeAll(sizesList);

                product.setSizes(sizesList);
                product.setColors(colorsList);
            }
            ProductSearchDto result = new ProductSearchDto();
            result.setResult(products);
            return result;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<ProductDto> getAllProductByIds(List<String> productIds) {
        List<ProductEntity>productEntities = productRepository.findAllById(productIds);
        List<ProductDto>result = IProductMapper.INSTANCE.toProductDtoListFromEntityList(productEntities);
        return result;
    }

    @Transactional
    @Override
    public void addProduct(ProductDto productDto) {
        try {
            ProductEntity productEntity = IProductMapper.INSTANCE.toProductEntityFromDto(productDto);
            productRepository.save(productEntity);
            for (SizesDto size : productDto.getSizes()) {
                ProductDto sizeOfProduct = new ProductDto(productDto.getId());
                size.setProduct(sizeOfProduct);
            }
            for (ColorsDto color : productDto.getColors()) {
                ProductDto colorOfProduct = new ProductDto(productDto.getId());
                color.setProduct(colorOfProduct);
            }

            List<SizesEntity> sizes = ISizesMapper.INSTANCE.toSizesEntityListFromDtoList(productDto.getSizes());
            List<ColorsEntity> colors = IColorsMapper.INSTANCE.toColorListFromDtoList(productDto.getColors());

            sizesRepository.saveAll(sizes);
            colorsRepository.saveAll(colors);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    @Override
    public void deleteProduct(String id) {
        try {
            List<ColorsEntity> colorsEntities = colorsRepository.findAllSizesByProductId(id);
            colorsRepository.deleteAll(colorsEntities);

            List<SizesEntity> sizesEntities = sizesRepository.findAllColorsbyProductId(id);
            sizesRepository.deleteAll(sizesEntities);

            productRepository.deleteById(id);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    @Override
    public void updateProduct(ProductDto productDto) {
        try {
            Optional<ProductEntity> optionalProductEntity = productRepository.findById(productDto.getId());

            ProductEntity existingProduct = optionalProductEntity.get();
            existingProduct.setName(productDto.getName());
            existingProduct.setPrice(productDto.getPrice());
            existingProduct.setStatus(productDto.getStatus());
            existingProduct.setDescription(productDto.getDescription());
            existingProduct.setImage(productDto.getImage());
            existingProduct.setSpecification(productDto.getSpecification());
            productRepository.save(existingProduct);

            for (SizesDto size : productDto.getSizes()) {
                ProductDto sizeOfProduct = new ProductDto(productDto.getId());
                List<String> sizeIds = productDto.getSizes().stream().map(SizesDto::getId).collect(Collectors.toList());
                for (String id : sizeIds) {
                    SizesDto sizeItemOfProduct = new SizesDto(id);
                    size.setId(sizeOfProduct.getId());
                    size.setProduct(sizeOfProduct);
                }
            }

            for (ColorsDto color : productDto.getColors()) {
                ProductDto colorOfProduct = new ProductDto(productDto.getId());
                List<String> colorIds = productDto.getColors().stream().map(ColorsDto::getId).collect(Collectors.toList());
                for (String id : colorIds) {
                    ColorsDto colorItemOfproduct = new ColorsDto(id);
                    color.setId(colorItemOfproduct.getId());
                    color.setProduct(colorOfProduct);
                }
            }

            List<SizesEntity> sizes = ISizesMapper.INSTANCE.toSizesEntityListFromDtoList(productDto.getSizes());
            List<ColorsEntity> colors = IColorsMapper.INSTANCE.toColorListFromDtoList(productDto.getColors());

            System.out.println(sizes);
            System.out.println(colors);

            sizesRepository.saveAll(sizes);
            colorsRepository.saveAll(colors);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    @Override
    public ProductDto seachProductForCode(String code) {
        try {
            // lay ds OptionProduct
            List<OptionProductEntity> optionProductEntities = optionProductRespository.findAll();
            List<OptionProductDto> optionProductDtos = IOptionProductMapper.INSTANCE.toListOptiondtoformEntity(optionProductEntities);

            ProductEntity productEntity = productRepository.findProductByCode(code);
            ProductDto productDto = IProductMapper.INSTANCE.toProductDtoFromEntity(productEntity);

            List<ColorsEntity> colorsEntities = colorsRepository.findAllSizesByProductId(productDto.getId());
            List<ColorsDto> colorsDtos = IColorsMapper.INSTANCE.toColorListFromEntityList(colorsEntities);
            for (ColorsDto colorsDto : colorsDtos) {
                List<OptionProductDto> result = optionProductDtos.stream()
                        .filter(option -> colorsDto.getOptionProduct().getId().equals(option.getId()))
                        .collect(Collectors.toList());

                colorsDto.setOptionProduct(result.size() == 0 ? null : result.get(0));
            }

            List<SizesEntity> sizesEntities = sizesRepository.findAllColorsbyProductId(productDto.getId());
            List<SizesDto> sizesDtos = ISizesMapper.INSTANCE.toSizesDtoListFromEntityList(sizesEntities);
            for (SizesDto sizesDto : sizesDtos) {
                List<OptionProductDto> result = optionProductDtos.stream()
                        .filter(option -> sizesDto.getOptionProduct().getId().equals(option.getId()))
                        .collect(Collectors.toList());
                sizesDto.setOptionProduct(result.size() == 0 ? null : result.get(0));
            }

            List<ColorsDto> colorsList = colorsDtos.stream()
                    .filter(obj -> obj.getProduct().getId().equals(productDto.getId()))
                    .collect(Collectors.toList());
            colorsList.forEach(color -> color.setProduct(null));
            colorsDtos.removeAll(colorsList);

            List<SizesDto> sizesList = sizesDtos.stream()
                    .filter(obj -> obj.getProduct().getId().equals(productDto.getId()))
                    .collect(Collectors.toList());
            sizesList.forEach(size -> size.setProduct(null));
            sizesDtos.removeAll(sizesList);

            productDto.setColors(colorsList);
            productDto.setSizes(sizesList);
            return productDto;
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    @Override
    public ProductSearchDto seachProductForName(String name) {
        try {
            // lay ds OptionProduct
            List<OptionProductEntity> optionProductEntities = optionProductRespository.findAll();
            List<OptionProductDto> optionProductDtos = IOptionProductMapper.INSTANCE.toListOptiondtoformEntity(optionProductEntities);

            List<ProductEntity> productEntities = productRepository.findProductByName(name);
            List<ProductDto> productDtos = IProductMapper.INSTANCE.toProductDtoListFromEntityList(productEntities);
            List<String> productIDs = productDtos.stream().map(ProductDto::getId).collect(Collectors.toList());

            List<ColorsEntity> colorsEntities = colorsRepository.findAllSizesByProductIds(productIDs);
            List<ColorsDto> colorsDtos = IColorsMapper.INSTANCE.toColorListFromEntityList(colorsEntities);
            for (ColorsDto colorsDto : colorsDtos) {
                List<OptionProductDto> result = optionProductDtos.stream()
                        .filter(option -> colorsDto.getOptionProduct().getId().equals(option.getId()))
                        .collect(Collectors.toList());

                colorsDto.setOptionProduct(result.size() == 0 ? null : result.get(0));
            }

            List<SizesEntity> sizesEntities = sizesRepository.findAllColorsbyProductIds(productIDs);
            List<SizesDto> sizesDtos = ISizesMapper.INSTANCE.toSizesDtoListFromEntityList(sizesEntities);
            for (SizesDto sizesDto : sizesDtos) {
                List<OptionProductDto> result = optionProductDtos.stream()
                        .filter(option -> sizesDto.getOptionProduct().getId().equals(option.getId()))
                        .collect(Collectors.toList());
                sizesDto.setOptionProduct(result.size() == 0 ? null : result.get(0));
            }

            for (ProductDto product : productDtos) {
                List<ColorsDto> colorsList = colorsDtos.stream()
                        .filter(obj -> obj.getProduct().getId().equals(product.getId()))
                        .collect(Collectors.toList());
                colorsList.forEach(color -> color.setProduct(null));
                colorsDtos.removeAll(colorsList);
                List<SizesDto> sizesList = sizesDtos.stream()
                        .filter(obj -> obj.getProduct().getId().equals(product.getId()))
                        .collect(Collectors.toList());
                sizesList.forEach(size -> size.setProduct(null));
                sizesDtos.removeAll(sizesList);

                product.setSizes(sizesList);
                product.setColors(colorsList);
            }
            ProductSearchDto result = new ProductSearchDto();
            result.setResult(productDtos);
            return result;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }
}
