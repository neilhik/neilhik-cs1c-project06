package lazyTrees;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FoothillTunesStore {

    private SongEntry[] arrayOfSongs;
    public static void main(String[] args)
    {
        FoothillTunesStore t = new FoothillTunesStore();

        try
        {
            // --------------------
            // parse the JSON file
            FileReader fileReader = new FileReader("resources/music_genre_subset.json");

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(fileReader);

            JSONArray allSongs = (JSONArray) jsonObject.get("songs");

            // --------------------
            // create an array of all the JSON objects
            t.arrayOfSongs = new SongEntry[allSongs.size()];

            Iterator<?> iterator = allSongs.iterator();
            int counter = 0;
            while (iterator.hasNext() && counter < t.arrayOfSongs.length)
            {
                JSONObject currentJson = (JSONObject)iterator.next();
                String title = currentJson.get("title").toString();
                int duration = (int)Math.ceil(Double.parseDouble(currentJson.get("duration").toString()));
                String artist = currentJson.get("artist_name").toString();
                String genre = currentJson.get("genre").toString();

                SongEntry currentSong = new SongEntry(title, duration, artist, genre);
                t.arrayOfSongs[counter++] = currentSong;
            }
        } // attempt to parse the input file
        catch (FileNotFoundException e)
        {	e.printStackTrace(); }
        catch (IOException e)
        {	e.printStackTrace(); }
        catch (ParseException e)
        {	e.printStackTrace(); }
        System.out.println(t.arrayOfSongs.length);

    }

}
