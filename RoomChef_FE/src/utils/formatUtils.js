/**
 * [RoomChef 화면 표시 및 계량 포맷팅 유틸]
 */

export function formatDifficulty(difficulty) {
  switch (difficulty?.toUpperCase()) {
    case 'EASY':
      return { text: '초간단', color: '#10b981' }
    case 'MEDIUM':
      return { text: '보통', color: '#f59e0b' }
    case 'HARD':
      return { text: '도전', color: '#ef4444' }
    default:
      return { text: '초간단', color: '#10b981' }
  }
}

export function formatTime(minutes) {
  if (!minutes) return '10분 컷'
  return `${minutes}분 소요`
}

export function formatRating(rating) {
  return rating ? Number(rating).toFixed(1) : '3.0'
}
