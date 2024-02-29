package app.user;

import app.Admin;
import app.audio.Collections.Playlist;
import app.audio.Files.Album;
import app.audio.Files.Song;
import app.player.PlayerSource;
import app.utils.Enums;
import fileio.input.CommandInput;
import visit.Visitor;

public final class Artist extends User {
    private int nrLikes;

    public Artist(final String username, final int age, final String city) {
        super(username, age, city);
    }

    public int getNrLikes() {
        return nrLikes;
    }

    public void setNrLikes(final int nrLikes) {
        this.nrLikes = nrLikes;
    }

    /**
     * Verifica daca exista deja o melodie intr-un album
     *
     * @param songName
     * @param artistName
     * @return
     */
    public int verifSong(final String songName, final String artistName) {
        // verifica daca exista deja o melodie cu acelasi nume
        for (Album album : Admin.getAlbumList()) {
            if (album.getOwner().equals(artistName)) {
                for (Song song : album.getSongs()) {
                    if (song.getName().equals(songName)) {
                        return 1;
                    }
                }
            }
        }
        return 0;
    }

    @Override
    public String accept(final Visitor visitor) {
        return visitor.visit(this);
    }

    /**
     * Verifica daca un artist poate fi sters
     *
     * @param commandInput
     * @return
     */
    public static String deleteArtist(final CommandInput commandInput) {
        String message;
        // verificare daca cineva ii asculta melodia
        for (User user : Admin.getUserList()) {
            if (user.getPlayer() != null && user.getPlayer().getSource() != null) {
                PlayerSource playerSource = user.getPlayer().getSource();
                if (playerSource.getType() == Enums.PlayerSourceType.LIBRARY) {
                    if (((Song) playerSource.getAudioFile()).getArtist().equals(commandInput.getUsername())) {
                        message = commandInput.getUsername() + " can't be deleted.";
                        return message;
                    }
                }
            }
        }

        // verificare daca cineva ii asculta albumul
        for (User user : Admin.getUserList()) {
            if (user.getPlayer() != null && user.getPlayer().getSource() != null) {
                PlayerSource playerSource = user.getPlayer().getSource();
                if (playerSource.getType() == Enums.PlayerSourceType.ALBUM) {
                    if (((Album) playerSource.getAudioCollection()).getOwner().equals(commandInput.getUsername())) {
                        message = commandInput.getUsername() + " can't be deleted.";
                        return message;
                    }
                }
            }
        }

        // verificare daca cineva asculta playlistul lui
        for (User user : Admin.getUserList()) {
            if (user.getPlayer() != null && user.getPlayer().getSource() != null) {
                PlayerSource playerSource = user.getPlayer().getSource();
                if (playerSource.getType() == Enums.PlayerSourceType.PLAYLIST) {
                    if (((Playlist) playerSource.getAudioCollection()).getOwner().equals(commandInput.getUsername())) {
                        message = commandInput.getUsername() + " can't be deleted.";
                        return message;
                    }
                }
            }
        }

        // verificare daca cineva asculta un playlist care are o melodie de-a lui
        for (User user : Admin.getUserList()) {
            if (user.getPlayer() != null && user.getPlayer().getSource() != null) {
                PlayerSource playerSource = user.getPlayer().getSource();
                message = Artist.verifPlaylist(playerSource, commandInput.getUsername());
                if (message != null) {
                    return message;
                }
            }
        }

        return null;
    }

    /**
     * verificare daca cineva asculta un playlist care are o melodie de-a lui
     *
     * @param playerSource
     * @param name
     * @return
     */
    public static String verifPlaylist(final PlayerSource playerSource, final String name) {
        String message;
        if (playerSource.getType() == Enums.PlayerSourceType.PLAYLIST) {
            for (Song song : ((Playlist) playerSource.getAudioCollection()).getSongs()) {
                if (song.getArtist().equals(name)) {
                    message = name + " can't be deleted.";
                    return message;
                }
            }
        }
        return null;
        
    }

    /**
     * Sterge melodiile de la LikeSong
     *
     * @param artist
     */
    public static void deleteLikeSong(final Album album) {
        for (User user : Admin.getUserList()) {
            for (int i = 0; i < user.getLikedSongs().size(); i++) {
                Song song = user.getLikedSongs().get(i);
                if (album.getSongs().contains(song)) {
                    user.getLikedSongs().remove(i);
                    i--;
                }
            }

        }
    }

}
