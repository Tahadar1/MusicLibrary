package com.example.MusicLibrary.favourie;

import com.example.MusicLibrary.music.Music;
import com.example.MusicLibrary.music.MusicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping(path = "api/v1/music/favourite")
public class FavouriteController {

    private final FavouriteService favouriteService;

    private final FavouriteRepository favouriteRepository;

    private final MusicRepository musicRepository;


    @Autowired
    public FavouriteController(FavouriteService favouriteService, FavouriteRepository favouriteRepository, MusicRepository musicRepository) {
        this.favouriteService = favouriteService;
        this.favouriteRepository = favouriteRepository;
        this.musicRepository = musicRepository;
    }

    @PostMapping("/add/{id}")
    public ResponseEntity<UploadFavouriteResponse> addToFavouriteSong(@PathVariable("id") Long id) {
        try {
            favouriteService.addToFavourite(id);
            UploadFavouriteResponse response = new UploadFavouriteResponse( true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            throw new IllegalStateException("Song is not added to favourite" + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<UploadFavouriteResponse>> getAllFavouriteSongs(){
        List<UploadFavouriteResponse> favSongs = favouriteService.getAllFavouriteSong().map(favouriteSong -> {
            String url = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("")
                    .path(String.valueOf(favouriteSong.getFilename()))
                    .toUriString();
            return new UploadFavouriteResponse(favouriteSong.getId(), favouriteSong.getIsFavourite(), favouriteSong.getFilename(), url);
        }).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(favSongs);
    }

    @GetMapping("/filename/{filename}")
    public ResponseEntity<UploadFavouriteResponse> getSongFromFavouriteByFilename(@PathVariable("filename") String filename){
        FavouriteSong favouriteSong = favouriteService.getSongByFilename(filename);
        String url = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("")
                .path(String.valueOf(favouriteSong.getFilename()))
                .toUriString();
        UploadFavouriteResponse response = new UploadFavouriteResponse(favouriteSong.getId(), favouriteSong.getIsFavourite(), favouriteSong.getFilename(), url);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/id/{id}")
    public void removeFromFavourite(@PathVariable("id") Long id){
        favouriteService.removeFromFavourite(id);
    }
}
