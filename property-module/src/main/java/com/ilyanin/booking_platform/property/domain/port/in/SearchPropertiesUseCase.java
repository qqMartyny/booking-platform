package com.ilyanin.booking_platform.property.domain.port.in;

import com.ilyanin.booking_platform.property.domain.model.Property;
import com.ilyanin.booking_platform.shared.PageResult;

public interface SearchPropertiesUseCase {
    PageResult<Property> searchByFilter(PropertySearchFilter filter);
}
