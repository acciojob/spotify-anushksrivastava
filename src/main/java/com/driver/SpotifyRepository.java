package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class SpotifyRepository {
    public HashMap<String, List<Album>> artistAlbumMap;
    public HashMap<String, List<Song>> albumSongMap;
    public HashMap<Playlist, List<Song>> playlistSongMap;
    public HashMap<Playlist, List<User>> playlistListenerMap;
    public HashMap<User, Playlist> creatorPlaylistMap;
    public HashMap<User, List<Playlist>> userPlaylistMap;
    public HashMap<Song, List<User>> songLikeMap;

    public List<User> users;
    public List<Song> songs;
    public List<Playlist> playlists;
    public List<Album> albums;
    public List<Artist> artists;



    public SpotifyRepository(){
        //To avoid hitting apis multiple times, initialize all the hashmaps here with some dummy data
        artistAlbumMap = new HashMap<String, List<Album>>();
        albumSongMap = new HashMap<String, List<Song>>();
        playlistSongMap = new HashMap<>();
        playlistListenerMap = new HashMap<>();
        creatorPlaylistMap = new HashMap<>();
        userPlaylistMap = new HashMap<>();
        songLikeMap = new HashMap<>();

        users = new ArrayList<>();
        songs = new ArrayList<>();
        playlists = new ArrayList<>();
        albums = new ArrayList<>();
        artists = new ArrayList<>();
    }

    public User createUser(String name, String mobile) {
        User user=new User(name,mobile);
        users.add(user);
        return user;
    }

    public Artist createArtist(String name) {
        Artist artist=new Artist(name);
        artists.add(artist);
        return artist;
    }

    public Album createAlbum(String title, String artistName) {
        boolean flag=false;
    for(int i=0;i<artists.size();i++)
    {
    Artist artist=artists.get(i);
    if(artist.getName().equals(artistName))
    {
        flag=true;
        break;
    }
    }
    if(flag==false)
    {
        Artist newArtist=new Artist(artistName);
        artists.add(newArtist);
    }
    Album newAlbum=new Album(title);
    albums.add(newAlbum);
        List<Album> AlbumList=new ArrayList<>();
        if(artistAlbumMap.containsKey(artistName)) {
            AlbumList = artistAlbumMap.get(artistName);
        }
    AlbumList.add(newAlbum);
    artistAlbumMap.put(artistName,AlbumList);
    return newAlbum;
    }

    public Song createSong(String title, String albumName, int length) throws Exception{
boolean found=false;
for(int i=0;i<albums.size();i++)
{
    Album album=albums.get(i);
    if(album.getTitle().equals(albumName))
    {
        found=true;
        break;
    }
}
if(found==false)
    throw new Exception("Album does not exist");

Song newSong=new Song(title,length);
songs.add(newSong);
        List<Song> SongList=new ArrayList<>();
        if(albumSongMap.containsKey(albumName)) {
            SongList = albumSongMap.get(albumName);
        }
SongList.add(newSong);
albumSongMap.put(albumName,SongList);
return newSong;
    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
Playlist newPlaylist=new Playlist(title);
   playlists.add(newPlaylist);

   for(int i=0;i< songs.size();i++)
   {
       if(songs.get(i).getLength()==length)
       {
           List<Song> SongList=new ArrayList<>();
           if(playlistSongMap.containsKey(newPlaylist)) {
              SongList = playlistSongMap.get(newPlaylist);
           }

           SongList.add(songs.get(i));

           playlistSongMap.put(newPlaylist,SongList);

       }
   }
   for(int i=0;i< users.size();i++)
   {
       if(users.get(i).getMobile().equals(mobile))
       {
           creatorPlaylistMap.put(users.get(i),newPlaylist);
           List<User> UserList=new ArrayList<>();
           if(playlistListenerMap.containsKey(newPlaylist)) {
               UserList = playlistListenerMap.get(newPlaylist);
           }
           UserList.add(users.get(i));
           playlistListenerMap.put(newPlaylist,UserList);

           List<Playlist> newPlay=new ArrayList<>();
           if(userPlaylistMap.containsKey(users.get(i)))
           {
               newPlay=userPlaylistMap.get(users.get(i));
           }
           newPlay.add(newPlaylist);
           userPlaylistMap.put(users.get(i),newPlay);
       }
   }
   return newPlaylist;
    }





    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        Playlist newPlaylist=new Playlist(title);
        playlists.add(newPlaylist);
        playlistSongMap.put(newPlaylist,new ArrayList<>());
        for(int i=0;i<songTitles.size();i++) {
            for (int j = 0; j < songs.size(); j++) {
                if (songTitles.get(i).equals(songs.get(j).getTitle())) {
                    playlistSongMap.get(newPlaylist).add(songs.get(j));
                }
            }
        }
            for(int i=0;i< users.size();i++)
            {
                if(users.get(i).getMobile().equals(mobile))
                {
                    creatorPlaylistMap.put(users.get(i),newPlaylist);
                    List<User> UserList=new ArrayList<>();
                    if(playlistListenerMap.containsKey(newPlaylist)) {
                        UserList = playlistListenerMap.get(newPlaylist);
                    }
                    UserList.add(users.get(i));
                    playlistListenerMap.put(newPlaylist,UserList);

                    List<Playlist> newPlay=new ArrayList<>();
                    if(userPlaylistMap.containsKey(users.get(i)))
                    {
                        newPlay=userPlaylistMap.get(users.get(i));
                    }
                    newPlay.add(newPlaylist);
                    userPlaylistMap.put(users.get(i),newPlay);
                }
            }

        return newPlaylist;
    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
        boolean found=false;
        Playlist pp=new Playlist();
        for(int i=0;i<playlists.size();i++)
        {
            Playlist playlist=playlists.get(i);
            if(playlist.getTitle().equals(playlistTitle))
            {
                found=true;
                pp=playlists.get(i);
                break;
            }
        }
        if(found==false)
            throw new Exception("Playlist does not exist");
User userr=new User();
        boolean locate=false;
        for(int i=0;i<users.size();i++)
        {
            User user=users.get(i);
            if(user.getMobile().equals(mobile))
            {
                locate=true;
                userr=user;
                break;
            }
        }
        if(locate==false)
            throw new Exception("User does not exist");

        for(int i=0;i<playlists.size();i++)
        {
            Playlist playlist=playlists.get(i);
            if(playlist.getTitle().equals(playlistTitle))
            {
                if(playlistListenerMap.containsKey(playlist))
                {
                    boolean flag=false;
                    List<User> us=playlistListenerMap.get(playlist);
                    for(int j=0;j<us.size();j++)
                    {
                        if(us.get(j).equals(userr))
                        {
                            flag=true;
                            break;
                        }
                    }
                    if(flag==false)
                    {
                        us.add(userr);
                        playlistListenerMap.put(playlist,us);
                    }
                }
            }

            }
        for(int i=0;i<users.size();i++) {
            User user = users.get(i);
            if (user.getMobile().equals(mobile)) {
                if (userPlaylistMap.containsKey(user)) {
                    userPlaylistMap.get(user).add(pp);
                    break;
                }
            }
        }
        return pp;

    }

    public Song likeSong(String mobile, String songTitle) throws Exception {
        boolean locate=false;
        User userr=new User();
        for(int i=0;i<users.size();i++)
        {
            User user=users.get(i);
            if(user.getMobile().equals(mobile))
            {
                userr=user;
                locate=true;
                break;
            }
        }
        if(locate==false)
            throw new Exception("User does not exist");

Song song=new Song();
        boolean found=false;
        for(int i=0;i< songs.size();i++)
        {
            if(songs.get(i).getTitle().equals(songTitle))
            {
                found=true;
                song=songs.get(i);
                break;
            }
        }

        if(found==false)
            throw new Exception("Song does not exist");

        boolean songLiked=false;
        for(int i=0;i< songs.size();i++)
        {
            List<User> al=new ArrayList<>();
            if(songLikeMap.containsKey(songs.get(i))) {
                al = songLikeMap.get(songs.get(i));

                for (int j = 0; j < al.size(); j++) {
                    if(al.get(j).equals(userr))
                    {
                        songLiked=true;
                    }

                }
            }
            if(songLiked==false) {
                al.add(userr);
                songs.get(i).setLikes(songs.get(i).getLikes() + 1);
            }
            songLiked=false;

        }
        String alb="";
        String art="";
        for(String str:albumSongMap.keySet()) {
            List<Song> al = albumSongMap.get(str);
            for (int j = 0; j < al.size(); j++) {
                if (al.get(j).getTitle().equals(songTitle)) {
                    alb = str;
                    break;
                }
            }
        }
            for(String s:artistAlbumMap.keySet())
            {
                List<Album> all=artistAlbumMap.get(s);
                for(int j=0;j< all.size();j++)
                {
                    if(all.get(j).getTitle().equals(alb))
                    {
                        art=s;
                        break;
                    }
                }
            }
            for(int i=0;i< artists.size();i++)
            {
                if(artists.get(i).getName().equals(art))
                {
                    artists.get(i).setLikes(artists.get(i).getLikes()+1);
                }
            }

            return song;
        }


    public String mostPopularArtist() {
        int likes=Integer.MIN_VALUE;
        String name="";
        for(int i=0;i< artists.size();i++)
        {
            Artist artist=artists.get(i);
            if(artist.getLikes()>likes)
            {
                likes= artist.getLikes();
                name=artist.getName();
            }
        }
        return name;
    }

    public String mostPopularSong() {
        int likes=Integer.MIN_VALUE;
        String name="";
        for(int i=0;i< songs.size();i++)
        {
            Song song=songs.get(i);
            if(song.getLikes()>likes)
            {
                likes= song.getLikes();
                name=song.getTitle();
            }
        }
        return name;
    }
}
