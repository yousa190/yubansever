import { createI18n } from 'vue-i18n'
import en from './en.json'
import zh from './zh.json'
import ja from './ja.json'




const i18n =createI18n({
    legacy: false,
    locale: localStorage.getItem('lang') || 'zh',
    messages: { en, zh ,ja },
})


i18n.t = (key) => i18n.global.t(key)

export default i18n
