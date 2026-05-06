-- Donnees de test - Topics
INSERT INTO topics (title, description, created_at)
SELECT 'JavaScript', 'Langage de programmation incontournable du web, utilise cote client comme cote serveur (Node.js).', NOW()
WHERE NOT EXISTS (SELECT 1 FROM topics WHERE title = 'JavaScript');

INSERT INTO topics (title, description, created_at)
SELECT 'Java', 'Langage oriente objet robuste et populaire, utilise pour le developpement back-end et les applications d entreprise.', NOW()
WHERE NOT EXISTS (SELECT 1 FROM topics WHERE title = 'Java');

INSERT INTO topics (title, description, created_at)
SELECT 'Python', 'Langage polyvalent et lisible, plebiscite pour la data science, le machine learning et le scripting.', NOW()
WHERE NOT EXISTS (SELECT 1 FROM topics WHERE title = 'Python');

INSERT INTO topics (title, description, created_at)
SELECT 'Web3', 'Technologies decentralisees basees sur la blockchain : smart contracts, DApps et cryptomonnaies.', NOW()
WHERE NOT EXISTS (SELECT 1 FROM topics WHERE title = 'Web3');

INSERT INTO topics (title, description, created_at)
SELECT 'TypeScript', 'Sur-ensemble typage de JavaScript, ideal pour les projets de grande envergure.', NOW()
WHERE NOT EXISTS (SELECT 1 FROM topics WHERE title = 'TypeScript');
