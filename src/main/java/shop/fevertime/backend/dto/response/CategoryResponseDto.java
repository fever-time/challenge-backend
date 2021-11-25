package shop.fevertime.backend.dto.response;

import lombok.Getter;
import lombok.Setter;
import shop.fevertime.backend.domain.Category;

@Getter
@Setter
public class CategoryResponseDto {

    private String name;
    private String htmlClassName;

    public CategoryResponseDto(Category category){
        this.name = category.getName();
        this.htmlClassName = category.getHtmlClassName();
    }
}
