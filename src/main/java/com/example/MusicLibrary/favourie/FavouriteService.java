package com.example.MusicLibrary.favourie;

import com.example.MusicLibrary.music.Music;
import com.example.MusicLibrary.music.MusicRepository;
import com.example.MusicLibrary.music.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;

@Service
public class FavouriteService {
    private final FavouriteRepository favouriteRepository;
    private final MusicRepository musicRepository;

    private final MusicService musicService;

    @Autowired
    public FavouriteService(FavouriteRepository favouriteRepository, MusicRepository musicRepository, MusicService musicService) {
        this.favouriteRepository = favouriteRepository;
        this.musicRepository = musicRepository;
        this.musicService = musicService;
    }

    public void addToFavourite(Long id) {
        Optional<FavouriteSong> favouriteSong = favouriteRepository.findById(id);
        if(favouriteSong.isPresent()){
            throw new IllegalStateException("Song is already in the favourite");
        }
        else {
            Optional<Music> music = musicRepository.findById(id);
            if (music.isEmpty()) {
                throw new IllegalStateException("Cannot add to favourite because song does not exists");
            }
            else {
                music.get().setIsFavorite(true);
                FavouriteSong favouriteSong1 = new FavouriteSong(music.get().getMusic_Id(), music.get().getFileName(), true);//music.stream().toList()
                favouriteRepository.save(favouriteSong1);
            }
        }
    }

    public Stream<FavouriteSong> getAllFavouriteSong(){
        return favouriteRepository.findAll().stream();
    }

    public void removeFromFavourite(Long id){
        Optional<FavouriteSong> favouriteSong = favouriteRepository.findById(id);
        Optional<Music> music = musicRepository.findById(id);
        if(!favouriteSong.isPresent()){
            throw new IllegalStateException("Song is not present in favourite list");
        }
        music.get().setIsFavorite(false);
        favouriteSong.get().setIsFavourite(false);
        favouriteRepository.deleteById(id);
    }

    public FavouriteSong getSongByFilename(String filename) {
        return favouriteRepository.findByFileName(filename).orElseThrow(() -> new IllegalStateException("Song is not present in the playlist"));
    }
}
