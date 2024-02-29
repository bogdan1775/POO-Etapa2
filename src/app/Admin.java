package app;

import app.audio.Collections.Playlist;
import app.audio.Collections.Podcast;
import app.audio.Files.Album;
import app.audio.Files.Announcement;
import app.audio.Files.Episode;
import app.audio.Files.Merch;
import app.audio.Files.Event;
import app.audio.Files.Song;
import app.player.PlayerSource;
import app.user.Artist;
import app.user.Host;
import app.user.User;
import app.utils.Enums;
import display.ShowPodcast;
import fileio.input.CommandInput;
import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import fileio.input.UserInput;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import visit.Visitor;

/**
 * The type Admin.
 */
public final class Admin {
    private static List<User> users = new ArrayList<>();
    private static List<Song> songs = new ArrayList<>();
    private static List<Podcast> podcasts = new ArrayList<>();
    private static int timestamp = 0;
    private static final int LIMIT = 5;
    private static List<Album> albums = new ArrayList<>();
    private static List<Event> events = new ArrayList<>();
    private static List<Merch> merchs = new ArrayList<>();
    private static List<Announcement> announcements = new ArrayList<>();

    private Admin() {
    }

    /**
     * Sets users.
     *
     * @param userInputList the user input list
     */
    public static void setUsers(final List<UserInput> userInputList) {
        users = new ArrayList<>();
        for (UserInput userInput : userInputList) {
            users.add(new User(userInput.getUsername(), userInput.getAge(), userInput.getCity()));
        }
    }

    /**
     * Sets songs.
     *
     * @param songInputList the song input list
     */
    public static void setSongs(final List<SongInput> songInputList) {
        songs = new ArrayList<>();
        for (SongInput songInput : songInputList) {
            songs.add(new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist()));
        }
    }

    /**
     * Sets podcasts.
     *
     * @param podcastInputList the podcast input list
     */
    public static void setPodcasts(final List<PodcastInput> podcastInputList) {
        podcasts = new ArrayList<>();
        for (PodcastInput podcastInput : podcastInputList) {
            List<Episode> episodes = new ArrayList<>();
            for (EpisodeInput episodeInput : podcastInput.getEpisodes()) {
                episodes.add(new Episode(episodeInput.getName(),
                        episodeInput.getDuration(),
                        episodeInput.getDescription()));
            }
            podcasts.add(new Podcast(podcastInput.getName(), podcastInput.getOwner(), episodes));
        }
    }

    /**
     * Gets songs.
     *
     * @return the songs
     */
    public static List<Song> getSongs() {
        return new ArrayList<>(songs);
    }

    /**
     * Gets podcasts.
     *
     * @return the podcasts
     */
    public static List<Podcast> getPodcasts() {
        return new ArrayList<>(podcasts);
    }

    /**
     * Gets playlists.
     *
     * @return the playlists
     */
    public static List<Playlist> getPlaylists() {
        List<Playlist> playlists = new ArrayList<>();
        for (User user : users) {
            playlists.addAll(user.getPlaylists());
        }
        return playlists;
    }

    /**
     * Gets user.
     *
     * @param username the username
     * @return the user
     */
    public static User getUser(final String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }

        return null;
    }

    /**
     * Update timestamp.
     *
     * @param newTimestamp the new timestamp
     */
    public static void updateTimestamp(final int newTimestamp) {
        int elapsed = newTimestamp - timestamp;
        timestamp = newTimestamp;
        if (elapsed == 0) {
            return;
        }

        for (User user : users) {
            if (user.getOnline()) {
                user.simulateTime(elapsed);
            }
        }
    }

    /**
     * Gets top 5 songs.
     *
     * @return the top 5 songs
     */
    public static List<String> getTop5Songs() {
        List<Song> sortedSongs = new ArrayList<>(songs);
        sortedSongs.sort(Comparator.comparingInt(Song::getLikes).reversed());
        List<String> topSongs = new ArrayList<>();
        int count = 0;
        for (Song song : sortedSongs) {
            if (count >= LIMIT) {
                break;
            }
            topSongs.add(song.getName());
            count++;
        }
        return topSongs;
    }

    /**
     * Gets top 5 playlists.
     *
     * @return the top 5 playlists
     */
    public static List<String> getTop5Playlists() {
        List<Playlist> sortedPlaylists = new ArrayList<>(getPlaylists());
        sortedPlaylists.sort(Comparator.comparingInt(Playlist::getFollowers)
                .reversed()
                .thenComparing(Playlist::getTimestamp, Comparator.naturalOrder()));
        List<String> topPlaylists = new ArrayList<>();
        int count = 0;
        for (Playlist playlist : sortedPlaylists) {
            if (count >= LIMIT) {
                break;
            }
            topPlaylists.add(playlist.getName());
            count++;
        }
        return topPlaylists;
    }

    /**
     * Reset.
     */
    public static void reset() {
        users = new ArrayList<>();
        songs = new ArrayList<>();
        podcasts = new ArrayList<>();
        albums = new ArrayList<>();
        events = new ArrayList<>();
        merchs = new ArrayList<>();
        announcements = new ArrayList<>();
        timestamp = 0;
    }

    /**
     * returneaza userii normali care sunt online
     *
     * @return
     */
    public static ArrayList<String> usersOnline() {
        ArrayList<String> result = new ArrayList<>();
        for (User user : users) {
            if (user.getOnline() && user.getType() == "normal") {
                result.add(user.getUsername());
            }

        }

        return result;
    }

    public static List<User> getUserList() {
        return users;
    }

    public static List<Song> getSongsList() {
        return songs;
    }

    public static List<Album> getAlbumList() {
        return albums;
    }

    public static List<Event> getEventList() {
        return events;
    }

    public static List<Merch> getMerchList() {
        return merchs;
    }

    public static List<Podcast> getPodcastList() {
        return podcasts;
    }

    public static List<Announcement> getAnnouncementList() {
        return announcements;
    }

    /**
     * adauga un utilizator
     *
     * @return rezultatul comenzii
     */
    public static String addUser(final CommandInput commandInput) {
        String message;
        String city = commandInput.getCity();
        String username = commandInput.getUsername();
        if (commandInput.getType().equals("artist")) {
            Artist artist = new Artist(username, commandInput.getAge(), city);
            artist.getPage().setTypePage("artist");
            artist.setType("artist");
            Admin.getUserList().add(artist);
            message = "The username " + username + " has been added successfully.";

        } else if (commandInput.getType().equals("user")) {
            User user2 = new User(username, commandInput.getAge(), city);
            user2.setType("normal");
            Admin.getUserList().add(user2);
            message = "The username " + username + " has been added successfully.";

        } else if (commandInput.getType().equals("host")) {
            Host host = new Host(commandInput.getUsername(), commandInput.getAge(), city);
            host.getPage().setTypePage("host");
            host.setType("host");
            Admin.getUserList().add(host);
            message = "The username " + username + " has been added successfully.";

        } else {
            message = "The type " + commandInput.getType() + " doesn't exist.";
        }
        return message;
    }

    /**
     * adauga un album
     *
     * @return
     */
    public static String addAlbum(final CommandInput commandInput) {
        String message;
        for (Album album : Admin.getAlbumList()) {
            if (album.getName().equals(commandInput.getName())
                    && album.getOwner().equals(commandInput.getUsername())) {
                message = commandInput.getUsername() + " has another album with the same name.";
                return message;
            }
        }

        // se verifica daca are de 2 ori o melodiei in album
        for (SongInput songInput : commandInput.getSongs()) {
            int sum = 0;
            for (SongInput songInput2 : commandInput.getSongs()) {
                if (songInput.getName().equals(songInput2.getName())) {
                    sum++;
                }
            }
            if (sum > 1) {
                message = commandInput.getUsername();
                message = message + " has the same song at least twice in this album.";
                return message;
            }
        }

        // creez albumul
        Album album = new Album(commandInput.getName());
        album.setName(commandInput.getName());
        album.setOwner(commandInput.getUsername());
        album.setReleaseYear(commandInput.getReleaseYear());
        album.setDescription(commandInput.getDescription());

        // adaug melodiile in album
        for (SongInput songInput : commandInput.getSongs()) {
            Song song = new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist());
            album.getSongs().add(song);
            Admin.getSongsList().add(song);
        }

        Admin.getAlbumList().add(album);

        message = commandInput.getUsername() + " has added new album successfully.";

        return message;
    }

    /**
     * printeaza pagina curenta a unui utilizator
     * folosesc Visitor pattern
     */
    public static String printCurrentPage(final CommandInput commandInput, final User user) {
        String message;
        Visitor visitor = new Page();
        message = user.accept(visitor);
        return message;
    }

    /**
     * adauga un eveniment
     *
     * @param commandInput
     */
    public static String addEvent(final CommandInput commandInput) {
        String message = null;
        for (Event event : Admin.getEventList()) {
            if (event.getName().equals(commandInput.getName())) {
                message = commandInput.getUsername() + " has another event with the same name.";
                return message;
            }
        }

        // verific daca data e valida
        message = Event.verificaData(commandInput);

        if (message != null) {
            return message;
        }

        // creez evenimentul
        Event event = new Event();
        event.setName(commandInput.getName());
        event.setOwner(commandInput.getUsername());
        event.setDescription(commandInput.getDescription());
        event.setDate(commandInput.getDate());

        Admin.getEventList().add(event);
        message = commandInput.getUsername() + " has added new event successfully.";

        return message;
    }

    /**
     * adauga un merch
     *
     * @param commandInput
     */
    public static String addMerch(final CommandInput commandInput) {
        String message;
        for (Merch merch : Admin.getMerchList()) {
            if (merch.getName().equals(commandInput.getName())) {
                message = commandInput.getUsername() + " has merchandise with the same name.";
                return message;
            }
        }

        // verific pretul
        if (commandInput.getPrice() < 0) {
            message = "Price for merchandise can not be negative.";
            return message;
        }

        // creez merch-ul
        Merch merch = new Merch();
        merch.setName(commandInput.getName());
        merch.setOwner(commandInput.getUsername());
        merch.setDescription(commandInput.getDescription());
        merch.setPrice(commandInput.getPrice());
        Admin.getMerchList().add(merch);
        message = commandInput.getUsername() + " has added new merchandise successfully.";

        return message;
    }

    /**
     * utitlizatorii online
     *
     * @param commandInput
     * @return
     */
    public static ArrayList<String> getAllUsers(final CommandInput commandInput) {
        ArrayList<String> result = new ArrayList<>();
        for (User user : Admin.getUserList()) {
            if (user.getType().equals("normal")) {
                result.add(user.getUsername());
            }
        }

        for (User user : Admin.getUserList()) {
            if (user.getType().equals("artist")) {
                result.add(user.getUsername());
            }
        }

        for (User user : Admin.getUserList()) {
            if (user.getType().equals("host")) {
                result.add(user.getUsername());
            }
        }

        return result;
    }

    /**
     * sterge un user
     *
     * @param commandInput
     * @return
     */
    public static String deleteUser(final CommandInput commandInput, final User user) {
        String message = null;
        if (user == null) {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
            return message;
        }

        // daca e artist se verifica daca poate fi sters
        message = Artist.deleteArtist(commandInput);
        if (message != null) {
            return message;
        }

        // daca e host se verifica daca poate fi sters
        message = Host.deleteHost(commandInput);
        if (message != null) {
            return message;
        }

        // verificare daca cineva se afla pe pagina lui
        for (User user2 : Admin.getUserList()) {
            if (user2.getType().equals("normal")) {
                if (user2.getPage().getTypePage().equals(user.getUsername())) {
                    message = commandInput.getUsername() + " can't be deleted.";
                    return message;
                }
            }
        }

        // sterg albumele lui
        List<Album> albums = Admin.getAlbumList();
        Album album;
        for (int k = 0; k < albums.size(); k++) {
            album = albums.get(k);
            if (album.getOwner().equals(commandInput.getUsername())) {
                for (Song song : album.getSongs()) {
                    Admin.getSongsList().remove(song);
                }
                Artist.deleteLikeSong(album);

                Admin.getAlbumList().remove(album);
            }
        }

        // sterge playlist de la FollowedPlaylists
        Playlist playlist;
        for (User user2 : Admin.getUserList()) {
            for (int i = 0; i < user2.getFollowedPlaylists().size(); i++) {
                playlist = user2.getFollowedPlaylists().get(i);
                if (playlist.getOwner().equals(commandInput.getUsername())) {
                    user2.getFollowedPlaylists().remove(i);
                    i--;
                }
            }

        }

        User.decrease(user);

        message = commandInput.getUsername() + " was successfully deleted.";
        Admin.getUserList().remove(user);
        return message;
    }

    /**
     * adauga un podcast
     *
     * @param commandInput
     */
    public static String addPodcast(final CommandInput commandInput, final User user) {
        String message;
        for (Podcast podcast : Admin.getPodcastList()) {
            if (podcast.getName().equals(commandInput.getName())) {
                message = commandInput.getUsername() + " has another podcast with the same name.";
                return message;
            }
        }

        // verific daca are un episod de 2 ori
        for (EpisodeInput episodeInput : commandInput.getEpisodes()) {
            int sum = 0;
            for (EpisodeInput episodeInput2 : commandInput.getEpisodes()) {
                if (episodeInput.getName().equals(episodeInput2.getName())) {
                    sum++;
                }
            }
            if (sum > 1) {
                message = commandInput.getUsername() + " has the same episode in this podcast.";
                return message;
            }
        }

        // adaug episoadele
        List<Episode> episodes = new ArrayList<>();
        for (EpisodeInput episodeInput : commandInput.getEpisodes()) {
            Episode episode = new Episode(episodeInput.getName(), episodeInput.getDuration(),
                    episodeInput.getDescription());
            episodes.add(episode);
        }

        Podcast podcast = new Podcast(commandInput.getName(), commandInput.getUsername(), episodes);
        Admin.getPodcastList().add(podcast);

        message = commandInput.getUsername() + " has added new podcast successfully.";

        return message;
    }

    /**
     * adauga un anunt
     *
     * @param commandInput
     * @return
     */
    public static String addAnnouncement(final CommandInput commandInput) {
        String message;
        for (Announcement announcement : Admin.getAnnouncementList()) {
            if (announcement.getName().equals(commandInput.getName())) {
                message = commandInput.getUsername();
                message = message + " has already added an announcement with this name.";
                return message;
            }
        }

        // creez anuntul
        Announcement announcement = new Announcement();
        announcement.setName(commandInput.getName());
        announcement.setOwner(commandInput.getUsername());
        announcement.setDescription(commandInput.getDescription());

        Admin.getAnnouncementList().add(announcement);
        message = commandInput.getUsername() + " has successfully added new announcement.";
        return message;
    }

    /**
     * sterge un anunt
     *
     * @param commandInput
     * @return
     */
    public static String removeAnnouncement(final CommandInput commandInput) {
        String message;
        for (Announcement announcement : Admin.getAnnouncementList()) {
            if (announcement.getName().equals(commandInput.getName())) {
                Admin.getAnnouncementList().remove(announcement);
                message = commandInput.getUsername();
                message = message + " has successfully deleted the announcement.";
                return message;
            }
        }

        message = commandInput.getUsername() + " has no announcement with the given name.";
        return message;
    }

    /**
     * afiseaza podcasturile unui host
     *
     * @param commandInput
     * @return
     */
    public static ArrayList<ShowPodcast> showPodcasts(final CommandInput commandInput) {
        ArrayList<ShowPodcast> showPodcasts = new ArrayList<>();
        for (Podcast podcast : Admin.getPodcastList()) {
            if (podcast.getOwner().equals(commandInput.getUsername())) {

                ShowPodcast showPodcast = new ShowPodcast();
                showPodcast.setName(podcast.getName());
                for (Episode episode : podcast.getEpisodes()) {
                    showPodcast.getEpisodes().add(episode.getName());
                }
                showPodcasts.add(showPodcast);
            }
        }
        return showPodcasts;
    }

    /**
     * sterge un album
     *
     * @param commandInput
     * @return
     */
    public static String removeAlbum(final CommandInput commandInput) {
        String message;
        Album albumArtist = null;
        for (Album album : Admin.getAlbumList()) {
            if (album.getName().equals(commandInput.getName())) {
                albumArtist = album;

            }
        }
        if (albumArtist == null) {
            message = commandInput.getUsername() + " doesn't have an album with the given name.";
            return message;
        }

        // verificare daca cineva asculta albumul
        for (User user : Admin.getUserList()) {
            if (user.getPlayer() != null && user.getPlayer().getSource() != null) {
                PlayerSource playerSource = user.getPlayer().getSource();
                if (playerSource.getType() == Enums.PlayerSourceType.ALBUM) {
                    String name = commandInput.getUsername();
                    if (((Album) playerSource.getAudioCollection()).getOwner().equals(name)) {
                        message = commandInput.getUsername() + " can't delete this album.";
                        return message;
                    }
                }
            }
        }

        // verific daca playlistul care e load are o melodie din album
        for (User user : Admin.getUserList()) {
            if (user.getPlayer() != null && user.getPlayer().getSource() != null) {
                PlayerSource playerSource = user.getPlayer().getSource();
                if (playerSource.getType() == Enums.PlayerSourceType.PLAYLIST) {
                    Playlist playlist = (Playlist) playerSource.getAudioCollection();
                    for (Song song : playlist.getSongs()) {
                        if (albumArtist.getSongs().contains(song)) {
                            message = commandInput.getUsername() + " can't delete this album.";
                            return message;
                        }
                    }
                }
            }
        }

        Admin.getAlbumList().remove(albumArtist);
        message = commandInput.getUsername() + " deleted the album successfully.";

        return message;

    }

    /**
     * schimba pagina unui user
     *
     * @param commandInput
     * @return
     */
    public static String changePage(final CommandInput commandInput, final User user) {
        String message;
        if (!commandInput.getNextPage().equals("Home")
                && !commandInput.getNextPage().equals("LikedContent")) {
            message = commandInput.getUsername() + " is trying to access a non-existent page.";
            return message;
        }
        user.getPage().setTypePage(commandInput.getNextPage());
        message = commandInput.getUsername();
        message = message + " accessed " + commandInput.getNextPage() + " successfully.";
        return message;
    }

    /**
     * sterge un eveniment
     *
     * @param commandInput
     * @return
     */
    public static String removeEvent(final CommandInput commandInput) {
        String message;
        Event eventArtist = null;
        for (Event event : Admin.getEventList()) {
            if (event.getName().equals(commandInput.getName())) {
                eventArtist = event;
            }
        }

        if (eventArtist == null) {
            message = commandInput.getUsername() + " doesn't have an event with the given name.";
        } else {
            message = commandInput.getUsername() + " deleted the event successfully.";
            Admin.getEventList().remove(eventArtist);
        }
        return message;
    }

    /**
     * sterge un podcast
     *
     * @param commandInput
     * @return
     */
    public static String removePodcast(final CommandInput commandInput) {
        String message;
        Podcast podcastHPodcast = null;
        for (Podcast podcast : Admin.getPodcastList()) {
            if (podcast.getName().equals(commandInput.getName())) {
                podcastHPodcast = podcast;
            }
        }

        if (podcastHPodcast == null) {
            message = commandInput.getUsername() + " doesn't have a podcast with the given name.";
        } else {
            // verificare daca cineva asculta Podcastul
            for (User user2 : Admin.getUserList()) {
                if (user2.getPlayer() != null && user2.getPlayer().getSource() != null) {
                    PlayerSource playerSource = user2.getPlayer().getSource();
                    if (playerSource.getType() == Enums.PlayerSourceType.PODCAST) {
                        if (((Podcast) playerSource.getAudioCollection()).getOwner()
                                .equals(commandInput.getUsername())) {
                            message = commandInput.getUsername() + " can't delete this podcast.";
                            return message;
                        }

                    }
                }
            }
            message = commandInput.getUsername() + " deleted the podcast successfully.";
            Admin.getPodcastList().remove(podcastHPodcast);
        }
        return message;
    }

    /**
     * afiseaza top 5 albume
     *
     * @param commandInput
     */
    public static ArrayList<String> getTop5Albums(final CommandInput commandInput) {
        // calculez numarul de like-uri pentru un album
        for (Album album : Admin.getAlbumList()) {
            int likes = 0;
            for (Song song : album.getSongs()) {
                likes += song.getLikes();
            }
            album.setLikes(likes);
        }

        List<Album> albums = Admin.getAlbumList();
        // sortez albumele
        Collections.sort(albums, (album1, album2) -> {
            if (album1.getLikes() == album2.getLikes()) {
                return album1.getName().compareTo(album2.getName());
            } else {
                return album2.getLikes() - album1.getLikes();
            }
        });

        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < albums.size() && i < LIMIT; i++) {
            result.add(albums.get(i).getName());
        }

        return result;
    }

    /**
     * afiseaza top 5 artisti
     *
     * @param commandInput
     */
    public static ArrayList<String> getTop5Artists(final CommandInput commandInput) {
        // calculez numarul de like-uri pentru un album
        for (Album album : Admin.getAlbumList()) {
            int likes = 0;
            for (Song song : album.getSongs()) {
                likes += song.getLikes();
            }
            album.setLikes(likes);
        }

        List<Artist> artists = new ArrayList<>();
        // calculez numarul de like-uri pentru un artist
        for (User user : Admin.getUserList()) {
            if (user.getType().equals("artist")) {
                int likes = 0;
                for (Album album : Admin.getAlbumList()) {
                    if (album.getOwner().equals(user.getUsername())) {
                        likes += album.getLikes();
                    }
                }
                ((Artist) user).setNrLikes(likes);
                artists.add((Artist) user);
            }
        }

        // sortez artistii
        Collections.sort(artists, (artist1, artist2) -> {
            if (artist1.getNrLikes() == artist2.getNrLikes()) {
                return artist1.getUsername().compareTo(artist2.getUsername());
            } else {
                return artist2.getNrLikes() - artist1.getNrLikes();
            }
        });

        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < artists.size() && i < LIMIT; i++) {
            result.add(artists.get(i).getUsername());
        }
        return result;
    }

}
