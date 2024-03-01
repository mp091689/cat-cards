package com.techelevator.controller;

import com.techelevator.dao.CatCardDao;
import com.techelevator.exception.DaoException;
import com.techelevator.model.CatCard;
import com.techelevator.services.CatFactService;
import com.techelevator.services.CatPicService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/cards")
public class CatController {

    private CatCardDao catCardDao;
    private CatFactService catFactService;
    private CatPicService catPicService;

    public CatController(CatCardDao catCardDao, CatFactService catFactService, CatPicService catPicService) {
        this.catCardDao = catCardDao;
        this.catFactService = catFactService;
        this.catPicService = catPicService;
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public CatCard get(@PathVariable int id) {
        CatCard card = catCardDao.getCatCardById(id);
        return card;
    }

    @GetMapping()
    public List<CatCard> getCatCardList() {
        List<CatCard> catCardList = catCardDao.getCatCards();
        return catCardList;
    }

    @RequestMapping(path = "/random", method = RequestMethod.GET)
    public CatCard randomCard() {
        CatCard newCard = new CatCard();
        newCard.setCatFact(catFactService.getFact().getText());
        newCard.setImgUrl(catPicService.getPic().getFile());

        return newCard;

    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST)
    public CatCard createCard(@Valid @RequestBody CatCard catCard) {
        CatCard createdCard = catCardDao.createCatCard(catCard);
        return createdCard;
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public void update(@Valid @RequestBody CatCard catCard, @PathVariable int id) {
        catCard.setCatCardId(id);
        catCardDao.updateCatCard(catCard);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public void delete(@Valid @PathVariable int id) {
        try {
            catCardDao.deleteCatCardById(id);
        } catch (DaoException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Delete was not successful");
        }
    }


}
