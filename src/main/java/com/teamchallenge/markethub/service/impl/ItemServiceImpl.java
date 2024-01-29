package com.teamchallenge.markethub.service.impl;

import com.teamchallenge.markethub.dto.item.ItemResponse;
import com.teamchallenge.markethub.error.ErrorMessages;
import com.teamchallenge.markethub.error.exception.ItemNotFoundException;
import com.teamchallenge.markethub.repository.ItemRepository;
import com.teamchallenge.markethub.service.ItemService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    @Override
    public ItemResponse findItemById(long id) throws ItemNotFoundException {
        return ItemResponse.convertToItemResponse(itemRepository.findById(id).orElseThrow(() ->
                new ItemNotFoundException(ErrorMessages.ITEM_NOT_FOUND.text())));
    }
}