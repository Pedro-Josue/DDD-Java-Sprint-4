const auth = {
    tokenKey: 'stockeasy_token',

    isAuthenticated() {
        return !!localStorage.getItem(this.tokenKey);
    },

    getToken() {
        return localStorage.getItem(this.tokenKey);
    },

    async login() {
        const login = document.getElementById('login').value;
        const senha = document.getElementById('senha').value;
        const errBox = document.getElementById('loginError');

        errBox.style.display = 'none';
        if (!login || !senha) {
            errBox.innerText = 'Preencha login e senha.';
            errBox.style.display = 'block';
            return;
        }

        try {
            const res = await fetch('/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ login, senha })
            });

            if (!res.ok) {
                const txt = await res.text();
                errBox.innerText = 'Falha no login. ' + (txt || res.statusText);
                errBox.style.display = 'block';
                return;
            }

            const j = await res.json();
            if (!j.token) {
                errBox.innerText = 'Resposta inesperada do servidor.';
                errBox.style.display = 'block';
                return;
            }

            localStorage.setItem(this.tokenKey, j.token);
            window.location.href = '/dashboard';
        } catch (e) {
            errBox.innerText = 'Erro ao conectar: ' + e.message;
            errBox.style.display = 'block';
        }
    },

    async register() {
        const login = document.getElementById('reg-login').value;
        const senha = document.getElementById('reg-senha').value;
        const senha2 = document.getElementById('reg-senha2').value;
        const errBox = document.getElementById('registerError');
        const okBox = document.getElementById('registerOk');

        errBox.style.display = 'none';
        okBox.style.display = 'none';

        if (!login || !senha) {
            errBox.innerText = 'Preencha login e senha.';
            errBox.style.display = 'block';
            return;
        }
        if (senha !== senha2) {
            errBox.innerText = 'As senhas não conferem.';
            errBox.style.display = 'block';
            return;
        }

        try {
            const res = await fetch('/user/novo', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ login, senha })
            });

            if (!res.ok) {
                const txt = await res.text();
                errBox.innerText = 'Erro ao criar usuário: ' + (txt || res.statusText);
                errBox.style.display = 'block';
                return;
            }

            okBox.innerText = 'Usuário criado com sucesso. Redirecionando para login...';
            okBox.style.display = 'block';
            setTimeout(() => { window.location.href = '/login'; }, 1300);
        } catch (e) {
            errBox.innerText = 'Erro ao conectar: ' + e.message;
            errBox.style.display = 'block';
        }
    },

    logout() {
        localStorage.removeItem(this.tokenKey);
        window.location.href = '/login';
    }
};

async function fetchWithAuth(url, opts = {}) {
    const token = auth.getToken();
    opts.headers = opts.headers || {};
    if (token) {
        opts.headers['Authorization'] = 'Bearer ' + token;
    }
    if (opts.body && !opts.headers['Content-Type']) {
        opts.headers['Content-Type'] = 'application/json';
    }
    const res = await fetch(url, opts);
    if (res.status === 401 || res.status === 403) {
        auth.logout();
        throw new Error('não autorizado');
    }
    return res;
}

const ui = {
    async loadDashboard() {
        try {
            const [mRes, cRes, mvRes] = await Promise.all([
                fetchWithAuth('/materiais'),
                fetchWithAuth('/categorias'),
                fetchWithAuth('/movimentacoes')
            ]);

            const materials = mRes.ok ? await mRes.json() : [];
            const categories = cRes.ok ? await cRes.json() : [];
            const movements = mvRes.ok ? await mvRes.json() : [];

            document.getElementById('count-materials').innerText = materials.length;
            document.getElementById('count-categories').innerText = categories.length;
            document.getElementById('count-movimentacoes').innerText = movements.length;

            const ms = document.getElementById('materials-section');
            ms.innerHTML = ui.renderMaterialsSection(materials, categories);

            const movSec = document.getElementById('movements-section');
            movSec.innerHTML = ui.renderMovementsTable(movements.slice(-8).reverse());

        } catch (e) {
            console.error(e);
        }
    },

    renderMaterialsSection(materials, categories) {
        const rows = materials.map(m => `
            <tr>
                <td>${m.id}</td>
                <td>${m.nome}</td>
                <td>${m.quantidade}</td>
                <td>${m.categoria ? m.categoria.nome : '-'}</td>
            </tr>
        `).join('');

        const categoryOptions = (categories || []).map(c => `<option value="${c.id}">${c.nome}</option>`).join('');

        const materialOptions = (materials || []).map(m =>
            `<option value="${m.id}">${m.id} - ${m.nome}</option>`
        ).join('');

        return `
            <div>
                <table>
                    <thead><tr><th>ID</th><th>Nome</th><th>Qtd</th><th>Categoria</th></tr></thead>
                    <tbody>${rows}</tbody>
                </table>

                <!-- Card de criar novo material -->
                <div style="margin-top:12px" class="card">
                    <h3>Novo material</h3>
                    <div class="form-group">
                        <label>Nome</label>
                        <input id="new-material-nome" />
                    </div>
                    <div class="form-group">
                        <label>Quantidade</label>
                        <input id="new-material-quantidade" type="number" value="0" />
                    </div>
                    <div class="form-group">
                        <label>Categoria</label>
                        <select id="new-material-categoria">
                            <option value="">-- selecione --</option>
                            ${categoryOptions}
                        </select>
                    </div>
                    <div class="form-actions">
                        <button class="btn-primary" onclick="ui.createMaterial()">Criar</button>
                    </div>
                    <p id="material-msg" class="muted"></p>
                </div>

                <!-- Card de excluir material -->
                <div style="margin-top:12px" class="card">
                    <h3>Excluir material</h3>
                    <div class="form-group">
                        <label>Selecionar material</label>
                        <select id="delete-material-select">
                            <option value="">-- selecione --</option>
                            ${materialOptions}
                        </select>
                    </div>
                    <div class="form-actions">
                        <button class="btn-primary" onclick="ui.deleteMaterialByInput()">Excluir</button>
                    </div>
                    <p id="delete-material-msg" class="muted"></p>
                </div>
            </div>
        `;
    },

    async deleteMaterialByInput() {
        const select = document.getElementById('delete-material-select');
        const id = parseInt(select.value, 10);
        const msg = document.getElementById('delete-material-msg');
        msg.style.display = 'none';

        if (!id) {
            msg.innerText = 'Selecione um material válido.';
            msg.style.display = 'block';
            return;
        }

        const materialName = select.options[select.selectedIndex].text;

        if (!confirm(`Deseja excluir o material "${materialName}"?`)) return;

        try {
            const res = await fetchWithAuth('/materiais/excluir/' + id, { method: 'DELETE' });
            if (!res.ok) {
                const t = await res.text();
                msg.innerText = 'Erro ao excluir: ' + (t || res.statusText);
                msg.style.display = 'block';
                return;
            }
            msg.innerText = 'Material excluído com sucesso.';
            msg.style.display = 'block';

            // Limpar seleção
            select.value = '';

            ui.loadDashboard();
        } catch (e) {
            console.error(e);
            msg.innerText = 'Erro ao excluir material.';
            msg.style.display = 'block';
        }
    },


    renderMovementsTable(movements) {
        if (!movements || movements.length === 0) {
            return '<p class="muted">Nenhuma movimentação encontrada.</p>';
        }
        const rows = movements.map(m => `
            <tr>
                <td>${m.id}</td>
                <td>${m.tipo || ''}</td>
                <td>${m.material ? m.material.nome : ''}</td>
                <td>${m.quantidade}</td>
                <td>${m.dataHora ? new Date(m.dataHora).toLocaleString() : ''}</td>
            </tr>
        `).join('');
        return `<table><thead><tr><th>ID</th><th>Tipo</th><th>Material</th><th>Qtd</th><th>Quando</th></tr></thead><tbody>${rows}</tbody></table>`;
    },

    async createMaterial() {
        const nome = document.getElementById('new-material-nome').value;
        const quantidade = parseInt(document.getElementById('new-material-quantidade').value || '0');
        const categoriaId = document.getElementById('new-material-categoria').value || null;

        const body = { nome, quantidade };
        if (categoriaId) body.categoria = { id: parseInt(categoriaId) };

        try {
            const res = await fetchWithAuth('/materiais/novo', { method: 'POST', body: JSON.stringify(body) });
            const msgEl = document.getElementById('material-msg');
            if (!res.ok) {
                const txt = await res.text();
                msgEl.innerText = 'Erro: ' + (txt || res.statusText);
                return;
            }
            msgEl.innerText = 'Material criado com sucesso.';
            ui.loadDashboard();
        } catch (e) {
            console.error(e);
        }
    },

    async startEditMaterial(id) {
        try {
            const res = await fetchWithAuth('/materiais');
            const arr = await res.json();
            const m = arr.find(x => x.id === id);
            if (!m) return alert('Material não encontrado');

            const novoNome = prompt('Nome', m.nome);
            if (novoNome === null) return;
            const novaQtd = prompt('Quantidade', m.quantidade);
            if (novaQtd === null) return;

            const body = { nome: novoNome, quantidade: parseInt(novaQtd, 10) };
            const r2 = await fetchWithAuth('/materiais/edita/' + id, { method: 'PUT', body: JSON.stringify(body) });
            if (!r2.ok) {
                const t = await r2.text();
                alert('Erro ao editar: ' + (t || r2.statusText));
                return;
            }
            ui.loadDashboard();
        } catch (e) {
            console.error(e);
        }
    },

    async deleteMaterial(id) {
        if (!confirm('Deseja excluir o material #' + id + '?')) return;
        try {
            const res = await fetchWithAuth('/materiais/excluir/' + id, { method: 'DELETE' });
            if (!res.ok) {
                const t = await res.text();
                alert('Erro ao excluir: ' + (t || res.statusText));
                return;
            }
            ui.loadDashboard();
        } catch (e) {
            console.error(e);
        }
    },

    showEntrada(materialId) {
        const qtd = prompt('Quantidade entrada para o material #' + materialId, '1');
        if (qtd === null) return;
        ui.registerEntrada(materialId, parseInt(qtd, 10));
    },

    showSaida(materialId) {
        const qtd = prompt('Quantidade saída para o material #' + materialId, '1');
        if (qtd === null) return;
        ui.registerSaida(materialId, parseInt(qtd, 10));
    },

    async registerEntrada(materialId, quantidade) {
        try {
            const body = { quantidade };
            const res = await fetchWithAuth(`/movimentacoes/entrada/${materialId}`, { method: 'POST', body: JSON.stringify(body) });
            if (!res.ok) {
                const t = await res.text();
                alert('Erro: ' + (t || res.statusText));
                return;
            }
            ui.loadDashboard();
            if (document.querySelector('#view-movimentacoes')) ui.loadMovementsView();
        } catch (e) { console.error(e); }
    },

    async registerSaida(materialId, quantidade) {
        try {
            const body = { quantidade };
            const res = await fetchWithAuth(`/movimentacoes/saida/${materialId}`, { method: 'POST', body: JSON.stringify(body) });
            if (!res.ok) {
                const t = await res.text();
                alert('Erro: ' + (t || res.statusText));
                return;
            }
            ui.loadDashboard();
            if (document.querySelector('#view-movimentacoes')) ui.loadMovementsView();
        } catch (e) { console.error(e); }
    },

    async loadMaterialsView() {
        try {
            const res = await fetchWithAuth('/materiais');
            const categoriesRes = await fetchWithAuth('/categorias');
            const materials = res.ok ? await res.json() : [];
            const categories = categoriesRes.ok ? await categoriesRes.json() : [];
            const main = document.getElementById('app') || document.querySelector('.container');
            if (!main) return;
            main.innerHTML = `<div class="card"><h2>Materiais</h2>${ui.renderMaterialsSection(materials, categories)}</div>`;
        } catch (e) { console.error(e); }
    },

    async loadCategoriesView() {
        try {
            const res = await fetchWithAuth('/categorias');
            const cats = res.ok ? await res.json() : [];
            const main = document.getElementById('app') || document.querySelector('.container');

            main.innerHTML = `
                <div class="card">
                    <h2>Categorias</h2>
                    ${this.renderCategoriesSection(cats)}
                </div>
            `;
        } catch (e) { console.error(e); }
    },

    renderCategoriesSection(categories) {
        const rows = categories.map(c => `
            <tr>
                <td>${c.id}</td>
                <td>${c.nome}</td>
            </tr>
        `).join('');

        const categoryOptions = (categories || []).map(c =>
            `<option value="${c.id}">${c.id} - ${c.nome}</option>`
        ).join('');

        return `
            <div>
                <table>
                    <thead><tr><th>ID</th><th>Nome</th></tr></thead>
                    <tbody>${rows}</tbody>
                </table>

                <!-- Card de criar nova categoria -->
                <div style="margin-top:12px" class="card">
                    <h3>Nova categoria</h3>
                    <div class="form-group">
                        <label>Nome</label>
                        <input id="new-cat-nome" />
                    </div>
                    <div class="form-actions">
                        <button class="btn-primary" onclick="ui.createCategory()">Criar</button>
                    </div>
                </div>

                <!-- Card de editar categoria -->
                <div style="margin-top:12px" class="card">
                    <h3>Editar categoria</h3>
                    <div class="form-group">
                        <label>Selecionar categoria</label>
                        <select id="edit-category-select">
                            <option value="">-- selecione --</option>
                            ${categoryOptions}
                        </select>
                    </div>
                    <div class="form-actions">
                        <button class="btn-primary" onclick="ui.editCategoryBySelect()">Editar</button>
                    </div>
                </div>

                <!-- Card de excluir categoria -->
                <div style="margin-top:12px" class="card">
                    <h3>Excluir categoria</h3>
                    <div class="form-group">
                        <label>Selecionar categoria</label>
                        <select id="delete-category-select">
                            <option value="">-- selecione --</option>
                            ${categoryOptions}
                        </select>
                    </div>
                    <div class="form-actions">
                        <button class="btn-primary" onclick="ui.deleteCategoryBySelect()">Excluir</button>
                    </div>
                </div>
            </div>
        `;
    },

    async createCategory() {
        const nome = document.getElementById('new-cat-nome').value;
        if (!nome) return alert('Preencha um nome');
        try {
            const res = await fetchWithAuth('/categorias/novo', { method: 'POST', body: JSON.stringify({ nome }) });
            if (!res.ok) {
                const t = await res.text();
                alert('Erro: ' + (t || res.statusText));
                return;
            }
            document.getElementById('new-cat-nome').value = '';
            ui.loadCategoriesView();
        } catch (e) { console.error(e); }
    },

    async editCategoryBySelect() {
        const select = document.getElementById('edit-category-select');
        const id = parseInt(select.value, 10);

        if (!id) {
            alert('Selecione uma categoria para editar.');
            return;
        }

        // Encontrar a categoria atual para preencher o prompt
        const currentName = select.options[select.selectedIndex].text.split(' - ')[1];
        const novo = prompt('Nome', currentName);
        if (novo === null) return;

        try {
            const res = await fetchWithAuth('/categorias/edita/' + id, { method: 'PUT', body: JSON.stringify({ nome: novo }) });
            if (!res.ok) {
                const t = await res.text();
                alert('Erro: ' + (t || res.statusText));
                return;
            }
            ui.loadCategoriesView();
        } catch (e) { console.error(e); }
    },

    async deleteCategoryBySelect() {
        const select = document.getElementById('delete-category-select');
        const id = parseInt(select.value, 10);

        if (!id) {
            alert('Selecione uma categoria para excluir.');
            return;
        }

        const categoryName = select.options[select.selectedIndex].text;
        if (!confirm(`Deseja excluir a categoria "${categoryName}"?`)) return;

        try {
            const res = await fetchWithAuth('/categorias/excluir/' + id, { method: 'DELETE' });
            if (!res.ok) {
                const t = await res.text();
                alert('Erro: ' + (t || res.statusText));
                return;
            }
            ui.loadCategoriesView();
        } catch (e) { console.error(e); }
    },

    async loadMovementsView() {
        try {
            const [res, matsRes] = await Promise.all([
                fetchWithAuth('/movimentacoes'),
                fetchWithAuth('/materiais')
            ]);
            const movements = res.ok ? await res.json() : [];
            const materials = matsRes.ok ? await matsRes.json() : [];

            const main = document.getElementById('app') || document.querySelector('.container');
            const materialsOptions = (materials || []).map(m => `<option value="${m.id}">${m.nome} (qtd: ${m.quantidade})</option>`).join('');

            main.innerHTML = `
                <section id="view-movimentacoes" class="view">
                    <div class="card">
                        <h2>Registrar movimentação</h2>
                        <div class="form-group">
                            <label>Material</label>
                            <select id="mv-material-id">${materialsOptions}</select>
                        </div>
                        <div class="form-group">
                            <label>Quantidade</label>
                            <input type="number" id="mv-quantidade" value="1" />
                        </div>
                        <div class="form-actions">
                            <button class="btn-primary" onclick="ui.handleRegister('entrada')">Registrar Entrada</button>
                            <button class="btn-primary" onclick="ui.handleRegister('saida')">Registrar Saída</button>
                        </div>
                        <p id="mv-msg" class="muted"></p>
                    </div>

                    <div class="card" style="margin-top:12px">
                        <h2>Movimentações</h2>
                        ${ui.renderMovementsTable(movements.slice().reverse())}
                    </div>
                </section>
            `;
        } catch (e) { console.error(e); }
    },

    async handleRegister(tipo) {
        const materialId = parseInt(document.getElementById('mv-material-id').value, 10);
        const quantidade = parseInt(document.getElementById('mv-quantidade').value || '0', 10);
        const msg = document.getElementById('mv-msg');
        msg.style.display = 'none';
        if (!materialId || !quantidade) {
            msg.innerText = 'Selecione material e quantidade válida.';
            msg.style.display = 'block';
            return;
        }
        try {
            if (tipo === 'entrada') {
                await ui.registerEntrada(materialId, quantidade);
            } else {
                await ui.registerSaida(materialId, quantidade);
            }
            msg.innerText = 'Movimentação registrada.';
            msg.style.display = 'block';
        } catch (e) {
            msg.innerText = 'Erro ao registrar movimentação.';
            msg.style.display = 'block';
            console.error(e);
        }
    }
};

function navigateTo(page) {
    if (!auth.isAuthenticated()) {
        window.location.href = '/login';
        return;
    }
    document.getElementById('btn-logout').style.display = 'inline-block';

    if (page === 'dashboard') {
        window.location.href = '/dashboard';
        return;
    }

    if (page === 'materials') ui.loadMaterialsView();
    if (page === 'categories') ui.loadCategoriesView();
    if (page === 'movimentacoes') ui.loadMovementsView();
}
