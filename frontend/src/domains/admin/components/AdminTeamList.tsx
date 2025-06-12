import React, { useState, useEffect } from 'react';
import { teamApi } from '../../api/teamApi';
import { TeamListItem, TeamCreateRequest } from '../../types';

interface AdminTeamListProps {
  onTeamSelect?: (team: TeamListItem) => void;
}

const AdminTeamList: React.FC<AdminTeamListProps> = ({ onTeamSelect }) => {
  const [teams, setTeams] = useState<TeamListItem[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [showCreateForm, setShowCreateForm] = useState(false);
  const [createForm, setCreateForm] = useState<TeamCreateRequest>({
    name: '',
    description: '',
    leader: ''
  });

  useEffect(() => {
    fetchTeams();
  }, []);

  const fetchTeams = async () => {
    try {
      setLoading(true);
      const data = await teamApi.admin.getAll();
      setTeams(data);
    } catch (err) {
      setError('팀 목록을 불러오는데 실패했습니다.');
      console.error('Error fetching teams:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleCreateTeam = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await teamApi.admin.create(createForm);
      setCreateForm({ name: '', description: '', leader: '' });
      setShowCreateForm(false);
      fetchTeams(); // 목록 새로고침
    } catch (err) {
      setError('팀 생성에 실패했습니다.');
      console.error('Error creating team:', err);
    }
  };

  const handleDeleteTeam = async (teamId: number) => {
    if (!window.confirm('정말로 이 팀을 삭제하시겠습니까?')) {
      return;
    }

    try {
      await teamApi.admin.delete(teamId);
      fetchTeams(); // 목록 새로고침
    } catch (err) {
      setError('팀 삭제에 실패했습니다.');
      console.error('Error deleting team:', err);
    }
  };

  if (loading) return <div className="text-center p-4">로딩 중...</div>;
  if (error) return <div className="text-red-500 text-center p-4">{error}</div>;

  return (
    <div className="max-w-6xl mx-auto p-6">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold text-gray-900">팀 관리</h1>
        <button
          onClick={() => setShowCreateForm(true)}
          className="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded-md transition-colors"
        >
          새 팀 추가
        </button>
      </div>

      {showCreateForm && (
        <div className="mb-6 bg-gray-50 p-4 rounded-lg">
          <h2 className="text-lg font-medium mb-4">새 팀 생성</h2>
          <form onSubmit={handleCreateTeam} className="space-y-4">
            <div>
              <label htmlFor="name" className="block text-sm font-medium text-gray-700">
                팀 이름 *
              </label>
              <input
                type="text"
                id="name"
                required
                value={createForm.name}
                onChange={(e) => setCreateForm({ ...createForm, name: e.target.value })}
                className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
              />
            </div>
            <div>
              <label htmlFor="description" className="block text-sm font-medium text-gray-700">
                팀 설명
              </label>
              <textarea
                id="description"
                value={createForm.description}
                onChange={(e) => setCreateForm({ ...createForm, description: e.target.value })}
                className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                rows={3}
              />
            </div>
            <div>
              <label htmlFor="leader" className="block text-sm font-medium text-gray-700">
                팀장
              </label>
              <input
                type="text"
                id="leader"
                value={createForm.leader}
                onChange={(e) => setCreateForm({ ...createForm, leader: e.target.value })}
                className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
              />
            </div>
            <div className="flex space-x-2">
              <button
                type="submit"
                className="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded-md transition-colors"
              >
                생성
              </button>
              <button
                type="button"
                onClick={() => setShowCreateForm(false)}
                className="bg-gray-500 hover:bg-gray-600 text-white px-4 py-2 rounded-md transition-colors"
              >
                취소
              </button>
            </div>
          </form>
        </div>
      )}

      <div className="bg-white shadow overflow-hidden sm:rounded-md">
        <ul className="divide-y divide-gray-200">
          {teams.map((team) => (
            <li key={team.id}>
              <div className="px-4 py-4 flex items-center justify-between">
                <div className="flex-1">
                  <div className="flex items-center">
                    <div className="flex-1">
                      <h3 className="text-lg font-medium text-gray-900">{team.name}</h3>
                      <p className="text-sm text-gray-500">
                        팀장: {team.leader || '미지정'} | 멤버: {team.memberCount}명
                      </p>
                      <span className={`inline-block px-2 py-1 text-xs rounded-full ${
                        team.status === 'ACTIVE' 
                          ? 'bg-green-100 text-green-800' 
                          : 'bg-red-100 text-red-800'
                      }`}>
                        {team.status === 'ACTIVE' ? '활성' : '비활성'}
                      </span>
                    </div>
                  </div>
                </div>
                <div className="flex space-x-2">
                  {onTeamSelect && (
                    <button
                      onClick={() => onTeamSelect(team)}
                      className="bg-blue-500 hover:bg-blue-600 text-white px-3 py-1 text-sm rounded-md transition-colors"
                    >
                      선택
                    </button>
                  )}
                  <button
                    onClick={() => handleDeleteTeam(team.id)}
                    className="bg-red-500 hover:bg-red-600 text-white px-3 py-1 text-sm rounded-md transition-colors"
                  >
                    삭제
                  </button>
                </div>
              </div>
            </li>
          ))}
        </ul>
        {teams.length === 0 && (
          <div className="text-center py-8 text-gray-500">
            등록된 팀이 없습니다.
          </div>
        )}
      </div>
    </div>
  );
};

export default AdminTeamList;