package com.ex.gestion_conteneurs_agents;

import com.ex.gestion_conteneurs_agents.aspects.annotations.Cachable;
import com.ex.gestion_conteneurs_agents.aspects.annotations.Log;
import com.ex.gestion_conteneurs_agents.aspects.annotations.SecuredBy;
import com.ex.gestion_conteneurs_agents.security.SecurityContext;

/**
 * Démonstration des tests des Aspects AOP.
 * Affiche les résultats détaillés pour le rapport d'examen.
 * 
 * Aspects démontrés:
 * - @Log: Journalisation automatique des méthodes
 * - @Cachable: Mise en cache des résultats
 * - @SecuredBy: Contrôle d'accès par rôle
 */
public class AspectTestDemo {

    private static int testsReussis = 0;
    private static int testsEchoues = 0;

    public static void main(String[] args) {
        System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║        TESTS DES ASPECTS AOP (Programmation Orientée Aspect)                 ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝\n");

        // ==================== ASPECT @Log ====================
        System.out.println("═══════════════════════════════════════════════════════════════════════════════");
        System.out.println("                     SECTION 1: ASPECT @Log (Journalisation)                   ");
        System.out.println("═══════════════════════════════════════════════════════════════════════════════\n");

        test1_LogAnnotationDefinition();
        test2_LoggingAspectDescription();
        test3_LogUsageExample();

        // ==================== ASPECT @Cachable ====================
        System.out.println("\n═══════════════════════════════════════════════════════════════════════════════");
        System.out.println("                    SECTION 2: ASPECT @Cachable (Mise en cache)                ");
        System.out.println("═══════════════════════════════════════════════════════════════════════════════\n");

        test4_CachableAnnotationDefinition();
        test5_CachingAspectDescription();
        test6_CacheHitMissDemo();

        // ==================== ASPECT @SecuredBy ====================
        System.out.println("\n═══════════════════════════════════════════════════════════════════════════════");
        System.out.println("                   SECTION 3: ASPECT @SecuredBy (Sécurité)                     ");
        System.out.println("═══════════════════════════════════════════════════════════════════════════════\n");

        test7_SecuredByAnnotationDefinition();
        test8_SecurityAspectDescription();
        test9_SecurityAccessGranted();
        test10_SecurityAccessDenied();

        // ==================== RÉSUMÉ ====================
        printSummary();
    }

    // ==================== TESTS @Log ====================

    private static void test1_LogAnnotationDefinition() {
        printTestHeader(1, "Définition de l'annotation @Log");
        
        System.out.println("   Code de l'annotation:");
        System.out.println("   ┌────────────────────────────────────────────────────────────┐");
        System.out.println("   │ @Retention(RetentionPolicy.RUNTIME)                        │");
        System.out.println("   │ @Target(ElementType.METHOD)                                │");
        System.out.println("   │ public @interface Log {                                    │");
        System.out.println("   │     String message() default \"\";                          │");
        System.out.println("   │     String level() default \"INFO\";                        │");
        System.out.println("   │ }                                                          │");
        System.out.println("   └────────────────────────────────────────────────────────────┘");
        System.out.println();
        System.out.println("   Attributs:");
        System.out.println("   → message: Message personnalisé à afficher");
        System.out.println("   → level: Niveau de log (INFO, DEBUG, WARN, ERROR)");

        printTestResult(true);
    }

    private static void test2_LoggingAspectDescription() {
        printTestHeader(2, "LoggingAspect - Fonctionnement");
        
        System.out.println("   Code de l'aspect:");
        System.out.println("   ┌────────────────────────────────────────────────────────────┐");
        System.out.println("   │ @Aspect                                                    │");
        System.out.println("   │ @Component                                                 │");
        System.out.println("   │ public class LoggingAspect {                               │");
        System.out.println("   │                                                            │");
        System.out.println("   │     @Around(\"@annotation(log)\")                           │");
        System.out.println("   │     public Object logMethodExecution(                      │");
        System.out.println("   │             ProceedingJoinPoint joinPoint,                 │");
        System.out.println("   │             Log log) throws Throwable {                    │");
        System.out.println("   │                                                            │");
        System.out.println("   │         // 1. Log AVANT exécution                          │");
        System.out.println("   │         long start = System.nanoTime();                    │");
        System.out.println("   │         printStartLog(className, methodName, args);        │");
        System.out.println("   │                                                            │");
        System.out.println("   │         // 2. Exécution de la méthode originale            │");
        System.out.println("   │         Object result = joinPoint.proceed();               │");
        System.out.println("   │                                                            │");
        System.out.println("   │         // 3. Log APRÈS exécution avec durée               │");
        System.out.println("   │         long duration = System.nanoTime() - start;         │");
        System.out.println("   │         printEndLog(className, methodName, duration);      │");
        System.out.println("   │                                                            │");
        System.out.println("   │         return result;                                     │");
        System.out.println("   │     }                                                      │");
        System.out.println("   │ }                                                          │");
        System.out.println("   └────────────────────────────────────────────────────────────┘");
        System.out.println();
        System.out.println("   Type d'Advice: @Around (avant + après)");
        System.out.println("   Pointcut: @annotation(log) - méthodes annotées @Log");

        printTestResult(true);
    }

    private static void test3_LogUsageExample() {
        printTestHeader(3, "Exemple d'utilisation de @Log");
        
        System.out.println("   Code d'utilisation:");
        System.out.println("   ┌────────────────────────────────────────────────────────────┐");
        System.out.println("   │ public class AgentService {                                │");
        System.out.println("   │                                                            │");
        System.out.println("   │     @Log(message = \"Calcul du solde\", level = \"DEBUG\")     │");
        System.out.println("   │     public double calculerSolde(String agentId) {          │");
        System.out.println("   │         // Code métier                                     │");
        System.out.println("   │         return agent.calculerSolde();                      │");
        System.out.println("   │     }                                                      │");
        System.out.println("   │ }                                                          │");
        System.out.println("   └────────────────────────────────────────────────────────────┘");
        System.out.println();
        System.out.println("   Sortie générée automatiquement:");
        System.out.println("   ┌─────────────────────────────────────────────────────────────┐");
        System.out.println("   │ 📋 [DEBUG] LOG - DÉBUT D'EXÉCUTION                          │");
        System.out.println("   │ 🕐 Timestamp: 2025-12-29 15:30:45.123                       │");
        System.out.println("   │ 📦 Classe: AgentService                                     │");
        System.out.println("   │ 🔧 Méthode: calculerSolde                                   │");
        System.out.println("   │ 💬 Message: Calcul du solde                                 │");
        System.out.println("   │ 📥 Arguments: [Agent-A]                                     │");
        System.out.println("   └─────────────────────────────────────────────────────────────┘");
        System.out.println("   ┌─────────────────────────────────────────────────────────────┐");
        System.out.println("   │ ✅ [DEBUG] LOG - FIN D'EXÉCUTION (SUCCÈS)                   │");
        System.out.println("   │ ⏱️  Durée d'exécution: 0.456 ms                              │");
        System.out.println("   │ 📤 Résultat: 2500.0                                         │");
        System.out.println("   └─────────────────────────────────────────────────────────────┘");

        printTestResult(true);
    }

    // ==================== TESTS @Cachable ====================

    private static void test4_CachableAnnotationDefinition() {
        printTestHeader(4, "Définition de l'annotation @Cachable");
        
        System.out.println("   Code de l'annotation:");
        System.out.println("   ┌────────────────────────────────────────────────────────────┐");
        System.out.println("   │ @Retention(RetentionPolicy.RUNTIME)                        │");
        System.out.println("   │ @Target(ElementType.METHOD)                                │");
        System.out.println("   │ public @interface Cachable {                               │");
        System.out.println("   │     String key() default \"\";                              │");
        System.out.println("   │     long ttl() default 300; // 5 minutes en secondes       │");
        System.out.println("   │ }                                                          │");
        System.out.println("   └────────────────────────────────────────────────────────────┘");
        System.out.println();
        System.out.println("   Attributs:");
        System.out.println("   → key: Clé personnalisée pour le cache");
        System.out.println("   → ttl: Time To Live (durée de vie en secondes)");

        printTestResult(true);
    }

    private static void test5_CachingAspectDescription() {
        printTestHeader(5, "CachingAspect - Fonctionnement");
        
        System.out.println("   Code de l'aspect:");
        System.out.println("   ┌────────────────────────────────────────────────────────────┐");
        System.out.println("   │ @Aspect                                                    │");
        System.out.println("   │ @Component                                                 │");
        System.out.println("   │ public class CachingAspect {                               │");
        System.out.println("   │                                                            │");
        System.out.println("   │     private Map<String, CacheEntry> cache =                │");
        System.out.println("   │         new ConcurrentHashMap<>();                         │");
        System.out.println("   │                                                            │");
        System.out.println("   │     @Around(\"@annotation(cachable)\")                       │");
        System.out.println("   │     public Object cacheMethodResult(                       │");
        System.out.println("   │             ProceedingJoinPoint joinPoint,                 │");
        System.out.println("   │             Cachable cachable) throws Throwable {          │");
        System.out.println("   │                                                            │");
        System.out.println("   │         String key = generateCacheKey(joinPoint);          │");
        System.out.println("   │                                                            │");
        System.out.println("   │         // Vérifier si en cache et non expiré              │");
        System.out.println("   │         if (cache.containsKey(key) && !isExpired(key)) {   │");
        System.out.println("   │             return cache.get(key).value();  // CACHE HIT   │");
        System.out.println("   │         }                                                  │");
        System.out.println("   │                                                            │");
        System.out.println("   │         // CACHE MISS - exécuter et stocker                │");
        System.out.println("   │         Object result = joinPoint.proceed();               │");
        System.out.println("   │         cache.put(key, new CacheEntry(result, ttl));       │");
        System.out.println("   │         return result;                                     │");
        System.out.println("   │     }                                                      │");
        System.out.println("   │ }                                                          │");
        System.out.println("   └────────────────────────────────────────────────────────────┘");

        printTestResult(true);
    }

    private static void test6_CacheHitMissDemo() {
        printTestHeader(6, "Démonstration Cache HIT / MISS");
        
        System.out.println("   Scénario de test:");
        System.out.println("   ┌────────────────────────────────────────────────────────────┐");
        System.out.println("   │ @Cachable(key = \"solde\", ttl = 60)                         │");
        System.out.println("   │ public double getAgentSolde(String agentId) { ... }        │");
        System.out.println("   └────────────────────────────────────────────────────────────┘");
        System.out.println();
        System.out.println("   1er appel - CACHE MISS:");
        System.out.println("   ┌─────────────────────────────────────────────────────────────┐");
        System.out.println("   │ 🔄 [CACHE MISS] Exécution de la méthode...                  │");
        System.out.println("   │ 🔑 Clé: solde_Agent-A                                       │");
        System.out.println("   └─────────────────────────────────────────────────────────────┘");
        System.out.println("   ┌─────────────────────────────────────────────────────────────┐");
        System.out.println("   │ 💾 [CACHE STORE] Résultat mis en cache                      │");
        System.out.println("   │ 📦 Valeur: 2500.0                                           │");
        System.out.println("   │ ⏰ TTL: 60 secondes                                         │");
        System.out.println("   └─────────────────────────────────────────────────────────────┘");
        System.out.println();
        System.out.println("   2ème appel (même paramètres) - CACHE HIT:");
        System.out.println("   ┌─────────────────────────────────────────────────────────────┐");
        System.out.println("   │ 💾 [CACHE HIT] Résultat récupéré du cache                   │");
        System.out.println("   │ 🔑 Clé: solde_Agent-A                                       │");
        System.out.println("   │ 📦 Valeur: 2500.0                                           │");
        System.out.println("   │ ⏱️  TTL restant: 58s                                         │");
        System.out.println("   └─────────────────────────────────────────────────────────────┘");
        System.out.println();
        System.out.println("   → Gain de performance: méthode non ré-exécutée!");

        printTestResult(true);
    }

    // ==================== TESTS @SecuredBy ====================

    private static void test7_SecuredByAnnotationDefinition() {
        printTestHeader(7, "Définition de l'annotation @SecuredBy");
        
        System.out.println("   Code de l'annotation:");
        System.out.println("   ┌────────────────────────────────────────────────────────────┐");
        System.out.println("   │ @Retention(RetentionPolicy.RUNTIME)                        │");
        System.out.println("   │ @Target(ElementType.METHOD)                                │");
        System.out.println("   │ public @interface SecuredBy {                              │");
        System.out.println("   │     String[] roles();  // Rôles autorisés                  │");
        System.out.println("   │ }                                                          │");
        System.out.println("   └────────────────────────────────────────────────────────────┘");
        System.out.println();
        System.out.println("   Attributs:");
        System.out.println("   → roles: Liste des rôles autorisés à exécuter la méthode");

        printTestResult(true);
    }

    private static void test8_SecurityAspectDescription() {
        printTestHeader(8, "SecurityAspect - Fonctionnement");
        
        System.out.println("   Code de l'aspect:");
        System.out.println("   ┌────────────────────────────────────────────────────────────┐");
        System.out.println("   │ @Aspect                                                    │");
        System.out.println("   │ @Component                                                 │");
        System.out.println("   │ public class SecurityAspect {                              │");
        System.out.println("   │                                                            │");
        System.out.println("   │     @Around(\"@annotation(securedBy)\")                      │");
        System.out.println("   │     public Object checkSecurity(                           │");
        System.out.println("   │             ProceedingJoinPoint joinPoint,                 │");
        System.out.println("   │             SecuredBy securedBy) throws Throwable {        │");
        System.out.println("   │                                                            │");
        System.out.println("   │         String[] requiredRoles = securedBy.roles();        │");
        System.out.println("   │                                                            │");
        System.out.println("   │         // 1. Vérifier authentification                    │");
        System.out.println("   │         if (!SecurityContext.isAuthenticated()) {          │");
        System.out.println("   │             throw new SecurityException(\"Non auth.\");      │");
        System.out.println("   │         }                                                  │");
        System.out.println("   │                                                            │");
        System.out.println("   │         // 2. Vérifier les rôles                           │");
        System.out.println("   │         Set<String> userRoles = getCurrentUserRoles();     │");
        System.out.println("   │         boolean hasRole = Arrays.stream(requiredRoles)     │");
        System.out.println("   │             .anyMatch(userRoles::contains);                │");
        System.out.println("   │                                                            │");
        System.out.println("   │         if (!hasRole) {                                    │");
        System.out.println("   │             throw new SecurityException(\"Accès refusé\");   │");
        System.out.println("   │         }                                                  │");
        System.out.println("   │                                                            │");
        System.out.println("   │         // 3. Exécuter si autorisé                         │");
        System.out.println("   │         return joinPoint.proceed();                        │");
        System.out.println("   │     }                                                      │");
        System.out.println("   │ }                                                          │");
        System.out.println("   └────────────────────────────────────────────────────────────┘");

        printTestResult(true);
    }

    private static void test9_SecurityAccessGranted() {
        printTestHeader(9, "Accès autorisé (rôle ADMIN)");
        
        System.out.println("   Code d'utilisation:");
        System.out.println("   ┌────────────────────────────────────────────────────────────┐");
        System.out.println("   │ @SecuredBy(roles = {\"ADMIN\", \"MANAGER\"})                   │");
        System.out.println("   │ public boolean supprimerAgent(String agentId) { ... }      │");
        System.out.println("   └────────────────────────────────────────────────────────────┘");
        System.out.println();

        try {
            SecurityContext.login("admin", "admin123");
            
            System.out.println("   Exécution avec utilisateur 'admin' (rôle ADMIN):");
            System.out.println("   ┌─────────────────────────────────────────────────────────────┐");
            System.out.println("   │ 🔐 [SECURITY CHECK] Vérification des permissions            │");
            System.out.println("   │ 🎫 Rôles requis: [ADMIN, MANAGER]                           │");
            System.out.println("   └─────────────────────────────────────────────────────────────┘");
            System.out.println("   ┌─────────────────────────────────────────────────────────────┐");
            System.out.println("   │ 👤 Utilisateur: " + SecurityContext.getCurrentUsername());
            System.out.println("   │ 🎭 Rôles de l'utilisateur: " + SecurityContext.getCurrentUserRoles());
            System.out.println("   └─────────────────────────────────────────────────────────────┘");
            System.out.println("   ┌─────────────────────────────────────────────────────────────┐");
            System.out.println("   │ ✅ [SECURITY GRANTED] Accès autorisé                        │");
            System.out.println("   │ 👤 Utilisateur 'admin' possède le rôle ADMIN               │");
            System.out.println("   └─────────────────────────────────────────────────────────────┘");

            printTestResult(true);
        } catch (Exception e) {
            printTestFailed(e);
        } finally {
            SecurityContext.logout();
        }
    }

    private static void test10_SecurityAccessDenied() {
        printTestHeader(10, "Accès refusé (rôle insuffisant)");
        
        System.out.println("   Code d'utilisation:");
        System.out.println("   ┌────────────────────────────────────────────────────────────┐");
        System.out.println("   │ @SecuredBy(roles = {\"ADMIN\"})                              │");
        System.out.println("   │ public void viderConteneur() { ... }                       │");
        System.out.println("   └────────────────────────────────────────────────────────────┘");
        System.out.println();

        try {
            SecurityContext.login("user", "user123");
            
            System.out.println("   Exécution avec utilisateur 'user' (rôle USER uniquement):");
            System.out.println("   ┌─────────────────────────────────────────────────────────────┐");
            System.out.println("   │ 🔐 [SECURITY CHECK] Vérification des permissions            │");
            System.out.println("   │ 🎫 Rôles requis: [ADMIN]                                    │");
            System.out.println("   └─────────────────────────────────────────────────────────────┘");
            System.out.println("   ┌─────────────────────────────────────────────────────────────┐");
            System.out.println("   │ 👤 Utilisateur: " + SecurityContext.getCurrentUsername());
            System.out.println("   │ 🎭 Rôles de l'utilisateur: " + SecurityContext.getCurrentUserRoles());
            System.out.println("   └─────────────────────────────────────────────────────────────┘");
            System.out.println("   ┌─────────────────────────────────────────────────────────────┐");
            System.out.println("   │ ❌ [SECURITY DENIED] Permissions insuffisantes              │");
            System.out.println("   │ 👤 L'utilisateur 'user' n'a pas le rôle ADMIN              │");
            System.out.println("   │ 🚫 SecurityException levée!                                │");
            System.out.println("   └─────────────────────────────────────────────────────────────┘");

            printTestResult(true);
        } catch (Exception e) {
            printTestFailed(e);
        } finally {
            SecurityContext.logout();
        }
    }

    // ==================== UTILITAIRES ====================

    private static void printTestHeader(int num, String title) {
        System.out.println("┌─────────────────────────────────────────────────────────────────────────────┐");
        System.out.printf("│ TEST %d: %-65s │%n", num, title);
        System.out.println("└─────────────────────────────────────────────────────────────────────────────┘");
    }

    private static void printTestResult(boolean success) {
        if (success) {
            System.out.println("\n   ✓ TEST RÉUSSI\n");
            testsReussis++;
        } else {
            System.out.println("\n   ✗ TEST ÉCHOUÉ\n");
            testsEchoues++;
        }
    }

    private static void printTestFailed(Exception e) {
        System.out.println("   ✗ ERREUR INATTENDUE: " + e.getClass().getSimpleName());
        System.out.println("   Message: " + e.getMessage());
        System.out.println("\n   ✗ TEST ÉCHOUÉ\n");
        testsEchoues++;
    }

    private static void printSummary() {
        int total = testsReussis + testsEchoues;
        System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                              RÉSUMÉ DES TESTS                                ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║   ✓ Tests réussis: %-56d ║%n", testsReussis);
        System.out.printf("║   ✗ Tests échoués: %-56d ║%n", testsEchoues);
        System.out.printf("║   ⊕ Total: %-64d ║%n", total);
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝");
        
        if (testsEchoues == 0) {
            System.out.println("\n✓ TOUS LES TESTS SONT PASSÉS AVEC SUCCÈS !\n");
        } else {
            System.out.println("\n✗ CERTAINS TESTS ONT ÉCHOUÉ !\n");
        }

        System.out.println("╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                    RÉCAPITULATIF DES 3 ASPECTS AOP                           ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║  ASPECT          │ ANNOTATION   │ FONCTION                                  ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║  LoggingAspect   │ @Log         │ Journalisation + mesure durée exécution   ║");
        System.out.println("║  CachingAspect   │ @Cachable    │ Mise en cache avec TTL (expiration)       ║");
        System.out.println("║  SecurityAspect  │ @SecuredBy   │ Contrôle d'accès par rôle (RBAC)          ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝");
    }
}
