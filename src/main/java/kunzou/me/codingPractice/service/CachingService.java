package kunzou.me.codingPractice.service;

public interface CachingService {
  String CACHE = "CACHE";

  void clearAllCache();
  void removeCustomer(Long id);
  void removeFilm(Long id);
}
