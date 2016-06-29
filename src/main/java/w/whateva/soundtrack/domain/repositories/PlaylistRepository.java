package w.whateva.soundtrack.domain.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import w.whateva.soundtrack.domain.Playlist;

/**
 * Created by rich on 4/3/16.
 */
public interface PlaylistRepository extends PagingAndSortingRepository<Playlist, Long> {
}
