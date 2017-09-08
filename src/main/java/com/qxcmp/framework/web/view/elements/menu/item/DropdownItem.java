package com.qxcmp.framework.web.view.elements.menu.item;

import com.qxcmp.framework.web.view.modules.dropdown.AbstractDropdown;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DropdownItem extends AbstractMenuItem {

    private AbstractDropdown dropdown;

    public DropdownItem(AbstractDropdown dropdown) {
        this.dropdown = dropdown;
    }

    @Override
    public String getFragmentName() {
        return "item-dropdown";
    }
}
