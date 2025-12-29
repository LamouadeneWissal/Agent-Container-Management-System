package com.ex.gestion_conteneurs_agents.aspects;

import com.ex.gestion_conteneurs_agents.aspects.annotations.Cachable;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Aspect de mise en cache (Caching).
 * 
 * Programmation Orientée Aspect (AOP):
 * - Intercepte les méthodes annotées avec @Cachable
 * - Stocke les résultats en cache pour éviter les recalculs
 * - Gère automatiquement l'expiration du cache (TTL)
 * - Améliore les performances pour les opérations coûteuses
 */
@Aspect
@Component
public class CachingAspect {

    /**
     * Structure pour stocker une entrée de cache avec son timestamp.
     */
    private record CacheEntry(Object value, LocalDateTime createdAt, long ttlSeconds) {
        boolean isExpired() {
            return LocalDateTime.now().isAfter(createdAt.plusSeconds(ttlSeconds));
        }
    }

    /**
     * Cache thread-safe utilisant ConcurrentHashMap.
     */
    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();

    /**
     * Advice Around qui intercepte les méthodes annotées @Cachable.
     * Vérifie si un résultat est en cache avant d'exécuter la méthode.
     */
    @Around("@annotation(cachable)")
    public Object cacheMethodResult(ProceedingJoinPoint joinPoint, Cachable cachable) throws Throwable {
        // Génération de la clé de cache
        String cacheKey = generateCacheKey(joinPoint, cachable);
        long ttl = cachable.ttl();

        // Vérification du cache
        CacheEntry cachedEntry = cache.get(cacheKey);
        
        if (cachedEntry != null && !cachedEntry.isExpired()) {
            System.out.println("\n┌─────────────────────────────────────────────────────────────┐");
            System.out.println("│ 💾 [CACHE HIT] Résultat récupéré du cache");
            System.out.println("│ 🔑 Clé: " + truncate(cacheKey, 50));
            System.out.println("│ 📦 Valeur: " + truncate(String.valueOf(cachedEntry.value()), 50));
            System.out.println("│ ⏰ Créé le: " + cachedEntry.createdAt());
            System.out.println("│ ⏱️  TTL restant: " + calculateRemainingTTL(cachedEntry) + "s");
            System.out.println("└─────────────────────────────────────────────────────────────┘");
            return cachedEntry.value();
        }

        // Cache miss ou expiré - exécution de la méthode
        System.out.println("\n┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│ 🔄 [CACHE MISS] Exécution de la méthode...");
        System.out.println("│ 🔑 Clé: " + truncate(cacheKey, 50));
        System.out.println("└─────────────────────────────────────────────────────────────┘");

        Object result = joinPoint.proceed();

        // Mise en cache du résultat
        if (result != null) {
            CacheEntry newEntry = new CacheEntry(result, LocalDateTime.now(), ttl);
            cache.put(cacheKey, newEntry);

            System.out.println("\n┌─────────────────────────────────────────────────────────────┐");
            System.out.println("│ 💾 [CACHE STORE] Résultat mis en cache");
            System.out.println("│ 🔑 Clé: " + truncate(cacheKey, 50));
            System.out.println("│ 📦 Valeur: " + truncate(String.valueOf(result), 50));
            System.out.println("│ ⏱️  TTL: " + ttl + "s");
            System.out.println("└─────────────────────────────────────────────────────────────┘");
        }

        return result;
    }

    /**
     * Génère une clé de cache unique basée sur la méthode et ses arguments.
     */
    private String generateCacheKey(ProceedingJoinPoint joinPoint, Cachable cachable) {
        if (!cachable.key().isEmpty()) {
            return cachable.key();
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        String argsHash = Arrays.toString(joinPoint.getArgs());

        return className + "." + methodName + "(" + argsHash + ")";
    }

    /**
     * Calcule le temps restant avant expiration du cache.
     */
    private long calculateRemainingTTL(CacheEntry entry) {
        LocalDateTime expirationTime = entry.createdAt().plusSeconds(entry.ttlSeconds());
        return java.time.Duration.between(LocalDateTime.now(), expirationTime).getSeconds();
    }

    /**
     * Tronque une chaîne si elle dépasse la longueur maximale.
     */
    private String truncate(String str, int maxLength) {
        if (str == null) return "null";
        if (str.length() <= maxLength) return str;
        return str.substring(0, maxLength) + "...";
    }

    /**
     * Invalide une entrée spécifique du cache.
     */
    public void invalidate(String key) {
        cache.remove(key);
        System.out.println("🗑️  Cache invalidé pour la clé: " + key);
    }

    /**
     * Vide entièrement le cache.
     */
    public void clearCache() {
        cache.clear();
        System.out.println("🧹 Cache entièrement vidé");
    }

    /**
     * Retourne la taille actuelle du cache.
     */
    public int getCacheSize() {
        return cache.size();
    }

    /**
     * Nettoie les entrées expirées du cache.
     */
    public void cleanExpiredEntries() {
        int initialSize = cache.size();
        cache.entrySet().removeIf(entry -> entry.getValue().isExpired());
        int removed = initialSize - cache.size();
        System.out.println("🧹 " + removed + " entrée(s) expirée(s) supprimée(s) du cache");
    }
}
